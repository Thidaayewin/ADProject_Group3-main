package com.team3.weather;

import com.team3.weather.service.EmailService;
import com.team3.weather.util.ThresholdType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.team3.weather.model.*;
import com.team3.weather.repository.*;
import com.team3.weather.security.SecurityConfig;
import com.team3.weather.util.ReadingCSV;

import java.time.LocalDateTime;

@SpringBootApplication
public class LaunchRegularApp {

    public static void main(String[] args) {
        SpringApplication.run(LaunchRegularApp.class, args);
    }

    @Bean
    CommandLineRunner initData(
            AdminRepository adminRepository,
            AccountHolderRepository accountHolderRepository,
            StationRepository stationRepository,
            RainfallRepository rainfallRepository,
            ReferenceRepository referenceRepository,
            PredictionModelRepository predictionModelRepository,
            ModelTrainLogRepository modelTrainLogRepository,
            ThresholdRepository thresholdRepository,
            EmailService emailService,
            ReadingCSV readingCSV) {
        return (args) -> {

            LocalDateTime now = LocalDateTime.now();
            String ykEm = "e1112632@u.nus.edu";

            // Persist ACCOUNT_HOLDER-------------------------------------------------

            if (adminRepository.findAll().size() == 0) {
                Admin a1 = new Admin(ykEm, now, ykEm, now, "Admin", "One",
                        "e1112632@u.nus.edu", SecurityConfig.passwordEncoder().encode("PWA1"), "99869900",
                        "A000001");
                Admin admin1 = adminRepository.saveAndFlush(a1);

            }
            // ADMINS

            String[] refNames = {
                    "EN_820", "EN_821", "EN_860", "EN_861", "EN_910", "EN_911", "EN_940", "EN_941",
                    "EN_970", "EN_971", "EN_020", "EN_021", "EN_040", "EN_041", "EN_060", "EN_061",
                    "EN_090", "EN_091", "EN_140", "EN_141", "EN_180", "EN_181"
            };

            String[] monthlyValues = {
                    "1982-04", "1983-07", "1986-03", "1987-09", "1991-05", "1992-07", "1994-09",
                    "1995-04", "1995-05", "1995-06", "2002-06", "2003-03", "2004-07", "2005-03",
                    "2006-09", "2007-02", "2009-07", "2010-04", "2014-10", "2016-05", "2018-09",
                    "2019-07"
            };

            String type = "ElNino";

            String[] refLnNames = {
                    "LN_830", "LN_831", "LN_840", "LN_841", "LN_880", "LN_881", "LN_950", "LN_951",
                    "LN_980", "LN_981", "LN_050", "LN_051", "LN_070", "LN_071", "LN_080", "LN_081",
                    "LN_100", "LN_101", "LN_110", "LN_111", "LN_160", "LN_161", "LN_170", "LN_171",
                    "LN_200", "LN_201", "LN_210", "LN_211"
            };

            String[] monthlyLnValues = {
                    "1983-09", "1984-02", "1984-10", "1985-09", "1988-05", "1989-06", "1995-08", "1996-04",
                    "1998-07", "2001-03", "2005-11", "2006-04", "2007-06", "2008-07", "2008-11", "2009-04",
                    "2010-06", "2011-06", "2011-07", "2012-05", "2016-08", "2017-01", "2017-10", "2018-05",
                    "2020-08", "2021-06", "2021-08", "2023-02"
            };

            String typeLn = "LaNina";

            if (referenceRepository.findAll().size() == 0) {
                for (int i = 0; i < refNames.length; i++) {
                    Reference ref = new Reference(refNames[i], monthlyValues[i], type);
                    referenceRepository.saveAndFlush(ref);
                }

                for (int i = 0; i < refLnNames.length; i++) {
                    Reference ref = new Reference(refLnNames[i], monthlyLnValues[i], typeLn);
                    referenceRepository.saveAndFlush(ref);
                }
            }
            Station s1 = new Station("S50 Clementi Rd", "clementi", 1.3337, 103.7768);
            Station s2 = new Station("S24 Upper Changi", "changi", 1.3678, 103.9826);
            if (stationRepository.findAll().size() == 0) {

                stationRepository.saveAndFlush(s1);
                stationRepository.saveAndFlush(s2);
            }

            if (predictionModelRepository.findAll().size() == 0) {

                PredictionModel m1 = new PredictionModel("0600", "NeuralProphet", s1, "1983-01", "1992-12", "Admin", now, true);
                PredictionModel m2 = new PredictionModel("0601", "NeuralProphet", s1, "1983-01", "2001-12", "Admin", now, false);
                PredictionModel m3 = new PredictionModel("0602", "NeuralProphet", s1, "1983-01", "2004-01", "Admin", now, false);
                PredictionModel m4 = new PredictionModel("0603", "NeuralProphet", s1, "1983-01", "2021-01", "Admin", now, false);
                PredictionModel m5 = new PredictionModel("0600", "NeuralProphet", s2, "1983-01", "1992-12", "Admin", now, true);
                PredictionModel m6 = new PredictionModel("0601", "NeuralProphet", s2, "1983-01", "2006-12", "Admin", now, false);
                PredictionModel m7 = new PredictionModel("0602", "NeuralProphet", s2, "1983-01", "2013-02", "Admin", now, false);
                PredictionModel m8 = new PredictionModel("0603", "NeuralProphet", s2, "1983-01", "2018-06", "Admin", now, false);
                predictionModelRepository.saveAndFlush(m1);
                predictionModelRepository.saveAndFlush(m2);
                predictionModelRepository.saveAndFlush(m3);
                predictionModelRepository.saveAndFlush(m4);
                predictionModelRepository.saveAndFlush(m5);
                predictionModelRepository.saveAndFlush(m6);
                predictionModelRepository.saveAndFlush(m7);
                predictionModelRepository.saveAndFlush(m8);
            }


            if (thresholdRepository.findAll().size() == 0) {
                Threshold dataThreshold = new Threshold(ykEm, now, ykEm, now, ThresholdType.DATA, "Percentile", 75);
                Threshold conceptThreshold = new Threshold(ykEm, now, ykEm, now, ThresholdType.CONCEPT, "Rolling RMSE Slope", 40);
                thresholdRepository.saveAndFlush(dataThreshold);
                thresholdRepository.saveAndFlush(conceptThreshold);
            }
            if (rainfallRepository.findAll().size() == 0) {
                readingCSV.run(args);
            }

        };
    }
}