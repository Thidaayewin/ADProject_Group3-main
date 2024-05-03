package com.team3.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.team3.weather.DataTransferObject.SaveModel;
import com.team3.weather.model.PredictionModel;
import com.team3.weather.model.Station;
import com.team3.weather.repository.PredictionModelRepository;
import com.team3.weather.repository.StationRepository;
import com.team3.weather.service.PredictionModelService;
import com.team3.weather.service.StationService;

import antlr.collections.List;
import antlr.collections.impl.LList;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
public class UploadController {

    //private static final String UPLOAD_DIR = "/opt/NP_Model/";
    private static final String UPLOAD_DIR = "C:\\Users\\xucon\\";
    @Autowired
    private PredictionModelService predictionModelService; // Inject the repository

    @Autowired
    private StationService stationService;

    public UploadController(PredictionModelService predictionModelService,StationService stationService) {
        this.predictionModelService = predictionModelService;
        this.stationService = stationService;
    } 


   



    @PostMapping("/upload")
    public String uploadFile(@RequestParam("files") MultipartFile[] files, Model model) {
        StringBuilder fileNames = new StringBuilder();

        for (MultipartFile file : files) {
            String originnalPath = UPLOAD_DIR + file.getOriginalFilename();
            File originnalFile = new File(originnalPath);
            if (file.isEmpty() || originnalFile.exists() || !file.getOriginalFilename().endsWith(".pkl")) {
                continue;
            }
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
                Files.write(path, bytes);
                fileNames.append(file.getOriginalFilename()).append(" ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("message", "Files uploaded: " + fileNames.toString());
        return "redirect:/";

        
        // final String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        
        // try {
        //     Path path = Paths.get(UPLOAD_DIR + fileName);
        //     Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        
        // attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');

        // return "rconcept_drift";
    }

    @PostMapping(path = "/delete")
   public String delete(@RequestParam("name") String name) 
         throws IOException {

      try {
         Files.deleteIfExists(Paths.get(UPLOAD_DIR + name));
      }

      catch (IOException e) {
         return "redirect:/";
      }
      return "concept_drift";
   }

///save model
// @PostMapping("/saveModelData")
//     @ResponseBody
//     public String saveModelData(@RequestBody PredictionModel formData) {
//         // Create a new PredictionModel instance and populate it with the form data
//         PredictionModel predictionModel = new PredictionModel();
//         Station station = stationService.getStationById(formData.getstat)
//         predictionModel.setStation(null);
//         predictionModel.setModelNumber(formData.getModelNumber());
//         predictionModel.setModelType(formData.getModelType());
        

//         // Save the predictionModel to the repository
//         //predictionModelRepository.save(predictionModel);
//         predictionModelService.createPredictionModel(predictionModel);

//         return "Model data saved successfully!";
//     }

    @PostMapping("/saveModelData")
    public ResponseEntity<String> saveModelData(@RequestBody SaveModel saveData) {
        PredictionModel predictionModel = new PredictionModel();
        Station station = stationService.findByStationValue(saveData.getWeatherStation());
        String max = predictionModelService.findMaxModelNumberByStationId(station.getStationId());
        predictionModel.setStation(station);
        predictionModel.setModelNumber(max+"");
        predictionModel.setModelType(saveData.getModel());
        predictionModel.setCreatedDate(LocalDateTime.now());
        predictionModel.setCreatedBy("Admin");
        predictionModel.setEnd_date(saveData.getEndDate());
        predictionModel.setStart_date(saveData.getStartDate());

        predictionModelService.createPredictionModel(predictionModel);
        return ResponseEntity.ok("Model data saved successfully");
    }
}


