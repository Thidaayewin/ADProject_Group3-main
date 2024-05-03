package com.team3.weather.scheduled;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.team3.weather.model.Admin;
import com.team3.weather.model.Rainfall;
import com.team3.weather.model.Station;
import com.team3.weather.model.Threshold;
import com.team3.weather.repository.RainfallRepository;
import com.team3.weather.repository.StationRepository;

import com.team3.weather.service.AdminService;
import com.team3.weather.service.EmailService;
import com.team3.weather.service.ThresholdService;
import com.team3.weather.util.ThresholdType;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableScheduling //(when need to import from API, please enable this annotation)
public class ScheduledService {

    private static final String TAINFALL_URL = "https://api.data.gov.sg/v1/environment/rainfall?date_time=";

    //get stations needed 
    private static final Set<String> STATION_ID = new HashSet<>(Arrays.asList("S50", "S24"));

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RainfallRepository rainfallRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ThresholdService thresholdService;

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("yyyy-MM");

    @Scheduled(cron = "0 0 2 1 * ?") //(when need to import from API, please enable this annotation)
    public void scheduledTasks() throws MessagingException, IOException {
        System.out.println("start scheduledTasks");
        Calendar beforeFirstMonthdate = getBeforeFirstMonthdate(-1, 5);
        Calendar nowFirstMonthdate = getBeforeFirstMonthdate(0, 0);
        List<Rainfall> resultTotal = new ArrayList<>();

        while (beforeFirstMonthdate.compareTo(nowFirstMonthdate) < 0) {
            JSONObject result = restTemplate.getForObject(TAINFALL_URL + FORMAT.format(beforeFirstMonthdate.getTime()), JSONObject.class);
            System.out.println(FORMAT.format(beforeFirstMonthdate.getTime()));
            // serialize data
            if (result != null) {
                JSONObject item = result.getJSONArray("items").getJSONObject(0);
                JSONArray stations = result.getJSONObject("metadata").getJSONArray("stations");
                stations = stations.stream().filter(station -> STATION_ID.contains(((Map<String, String>) station)
                        .get("id"))).collect(Collectors.toCollection(JSONArray::new));
                JSONArray rainfallArry = item.getJSONArray("readings");
                String timestamp = item.getString("timestamp");
                for (int i = 0; i < rainfallArry.size(); i++) {
                    JSONObject rainfallTempJson = rainfallArry.getJSONObject(i);
                    String stationId = rainfallTempJson.getString("station_id");
                    Station station = stationRepository.findByStationName(stationId);
                    if (station == null) {
                        JSONArray jsonArray = stations.stream().filter(stationOne -> stationId.equals(((Map<String, String>) stationOne)
                                .get("id"))).collect(Collectors.toCollection(JSONArray::new));
                        if (!jsonArray.isEmpty()) {
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            double latitude = jsonObject.getJSONObject("location").getDouble("latitude");
                            double longitude = jsonObject.getJSONObject("location").getDouble("longitude");
                            station = new Station();
                            station.setStationName(stationId);
                            station.setLatitude(latitude);
                            station.setLongitude(longitude);
                            stationRepository.save(station);
                        }
                    }
                    if (STATION_ID.contains(stationId)) {
                        Rainfall rainfall = new Rainfall(station, timestamp, rainfallTempJson.getDoubleValue("value"));
                        resultTotal.add(rainfall);
                    }
                }
            }
            beforeFirstMonthdate.add(Calendar.MINUTE, 5);
        }
        Map<String, List<Rainfall>> stationMap = resultTotal.stream().collect(Collectors
                .groupingBy(rainfall -> rainfall.getStation().getStationName()));
        List<Rainfall> resultCount = new ArrayList<>();
        beforeFirstMonthdate.add(Calendar.MONTH, -1);
        for (Map.Entry<String, List<Rainfall>> entry : stationMap.entrySet()) {
            double sum = entry.getValue().stream().mapToDouble(Rainfall::getActualRainfallAmount).sum();
            System.out.println("sum:" + sum);
            Station station = entry.getValue().get(0).getStation();
            Rainfall rainfall = new Rainfall(station, MONTH_FORMAT.format(beforeFirstMonthdate.getTime()), sum);
            resultCount.add(rainfall);
        }
        System.out.println("start save data");
        rainfallRepository.saveAllAndFlush(resultCount);
        System.out.println("end data retrieval #########");

//        boolean conceptDriftChecker = evaluateConceptDriftThreshold_Slope().entrySet().stream()
//                .map(Map.Entry::getValue)  // Extract values from the key-value pairs
//                .anyMatch(value -> value.equals(true)); // Check if any value is true

        //use boolean to not repeatedly call API
        boolean dataDriftChecker_Percentiles = evaluateDataDriftThreshold_Percentile();
        Map<String, Boolean> conceptDriftEvaluationList = evaluateConceptDriftThreshold_Slope();    //TODO: implement in the future for more complex settings
        boolean conceptDriftChecker_Slope = conceptDriftEvaluationList.values().stream().anyMatch(value -> value/*.equals(true)*/);

        if (dataDriftChecker_Percentiles || conceptDriftChecker_Slope) {
            List<Admin> admins = adminService.findAllAdmins();
            if (admins.size() == 0)
                return;

            String dataDriftDetails = "The data drift threshold was not crossed this month.";
            String conceptDriftDetails = "The concept drift threshold was not crossed this month.";
            List<Threshold> thresholds = thresholdService.getAllThresholds();

            if (dataDriftChecker_Percentiles) {
                Threshold threshold_data = thresholds.stream().filter(x->x.getType().equals(ThresholdType.DATA)).findFirst().get();
                dataDriftDetails = "The data drift threshold for "+threshold_data.getMetric()+ " which was set at "+ threshold_data.getStatistic() + "was crossed.";
            }
            if (conceptDriftChecker_Slope) {
                Threshold threshold_concept = thresholds.stream().filter(x->x.getType().equals(ThresholdType.CONCEPT)).findFirst().get();
                conceptDriftDetails = "The data drift threshold for "+ threshold_concept.getMetric()+ " which was set at "+ threshold_concept.getStatistic() + "was crossed.";
            }

            for (Admin admin : admins) {
                emailService.sendThresholdCrossedEmail(admin.getEmail(), admin.getFirstName() + " " + admin.getLastName(), dataDriftDetails, conceptDriftDetails);
            }
        }
        System.out.println("end drift checking #########");
    }

    private Map<String, Boolean> evaluateConceptDriftThreshold_Slope() {
        //call this endpoint: http://8.222.245.68:8080/drift/concept/slope?type=neuralprophet&wRMSE=12
        //returns a json with values for every station
        //  1. retrieve threshold
        //  2. compare universal concept drift threshold (slope) with slope for each station's value
        //  3. append if each station passes/fails (T = send email, F = no email)
        HashMap<String, Boolean> evaluationResult = new HashMap<>();
        return evaluationResult;

    }

    private boolean evaluateDataDriftThreshold_Percentile() {
        //call this endpoint: http://8.222.245.68:8080/drift/concept/slope?type=neuralprophet&wRMSE=12
        //returns a json with values for similar' previous months
        //compare recent month to 'similar' previous months' percentiles
        //  1. retrieve threshold
        //  2. which threshold to match (25, 50, 75)
        //  3. compare if value is above or below that threshold. Below 75%- alert T if above; Below 50%- alert T if above; Below 25%- alert T if above  // ??old- don't use next line: 3. compare 4 conditions (0-25, 25-50, 50-75, 75-100)
        return false;
    }

    private static Calendar getBeforeFirstMonthdate(int amount, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, amount);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }
}
