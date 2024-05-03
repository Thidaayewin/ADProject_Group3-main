package com.team3.weather.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.team3.weather.model.Rainfall;
import com.team3.weather.model.Station;
import com.team3.weather.repository.RainfallRepository;
import com.team3.weather.repository.StationRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.meta.When;
import javax.swing.Spring;

@Component
public class ReadingCSV  {      // when need to use please add "implements CommandLineRunner"

    @Autowired
    private RainfallRepository rainfallRepository;

    @Autowired
    private StationRepository stationRepository;

    private static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("yyyy-MM");

    //@Override  (when needed to export, enable it)
    public void run(String... args) throws Exception {
        loadData();
        //saveData("C:\\Users\\xucon\\Downloads\\OneDrive_2023-08-10\\Historical monthly data for upload to DB\\Clementi Road_processed_daily_after.csv", "S50", 1.3337, 103.7768);
        //saveData("C:\\Users\\xucon\\Downloads\\OneDrive_2023-08-10\\Historical monthly data for upload to DB\\Changi_processed_daily_after.csv", "S24", 1.3678, 103.9826);
        // saveData("C:\\Users\\Abigail\\Documents\\GDIPSA\\adproj_downloaddata\\4. Univariate Model_Daily\\Clementi Road_processed_daily_after.csv", "clementi", 1.3337, 103.7768);
        // saveData("C:\\Users\\Abigail\\Documents\\GDIPSA\\adproj_downloaddata\\4. Univariate Model_Daily\\Changi_processed_daily_after.csv", "changi", 1.3678, 103.9826);
    }

    public void loadData() {
        try {
            ClassPathResource clementiResource = new ClassPathResource("Clementi Road_processed_daily_after.csv");
            File clementiFile = clementiResource.getFile();
            saveData(clementiFile.getAbsolutePath(), "clementi", 1.3337, 103.7768);

            ClassPathResource changiResource = new ClassPathResource("Changi_processed_daily_after.csv");
            File changiFile = changiResource.getFile();
            saveData(changiFile.getAbsolutePath(), "changi", 1.3678, 103.9826);
        } catch (IOException e) {
        // Handle IOException appropriately
        }
    }

    private void saveData(String filePath, String stationName,  double latitude, double longitude) {
        try {
            List<List<String>> data = new ArrayList<>();//list of lists to store data
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);

            //Reading until we run out of lines
            String line = br.readLine();
            int num = 0;
            List<Rainfall> rainfalls = new ArrayList<>();

            Station station = stationRepository.findByStationValue(stationName);
            if (station == null) {
                station = new Station("",stationName, latitude, longitude);
                stationRepository.saveAndFlush(station);
            }
            line = br.readLine();
            while(line != null) {
                List<String> lineData = Arrays.asList(line.split(","));//splitting lines
                Rainfall rainfall1 = new Rainfall(station, lineData.get(0),
                        Double.parseDouble(lineData.get(1)));
                rainfalls.add(rainfall1);
                if (rainfalls.size() >= 10000) {
                    num = num+10000;
                    System.out.println(num);
                    rainfallRepository.saveAllAndFlush(rainfalls);
                    rainfalls = new ArrayList<>();
                }
                line = br.readLine();
            }
            if (rainfalls.size() > 0) {
                rainfallRepository.saveAllAndFlush(rainfalls);
                System.out.println("Successfully exported to Database");
            }
            br.close();
            System.out.println("end###########");
        } catch(Exception e) {
            System.out.print(e);
        }
    }
}
