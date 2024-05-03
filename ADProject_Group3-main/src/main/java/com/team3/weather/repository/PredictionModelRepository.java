package com.team3.weather.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.team3.weather.model.PredictionModel;
import com.team3.weather.model.Station;

@Repository
public interface PredictionModelRepository extends JpaRepository<PredictionModel, Integer> {
    Optional<PredictionModel> findByIsDefaultTrue();
    List<PredictionModel> findByIsDefaultAndStation(boolean isDefault, Station station);
    @Query("SELECT DISTINCT p.modelNumber, p.modelType FROM PredictionModel p")
    List<Object[]> findDistinctModelNumbersAndTypes();
    @Query("SELECT DISTINCT p.modelType FROM PredictionModel p")
    List<Object[]> findDistinctModelTypes();
    @Query("SELECT pm FROM PredictionModel pm WHERE pm.modelType = ?1 AND pm.station.stationId = ?2")
    List<PredictionModel> findByModelTypeAndStationId(String modelType, int stationId);

       @Query("SELECT pm.end_date FROM PredictionModel pm " +
       "WHERE pm.station.stationId = (SELECT s.stationId FROM Station s WHERE s.stationValue = :stationName) " +
       "AND (pm.modelNumber = :modelType " +
       "OR (pm.modelNumber <> :modelType " +
       "AND pm.end_date <= (SELECT pmSelected.end_date FROM PredictionModel pmSelected " +
       "WHERE pmSelected.modelNumber = :modelType " +
       "AND pmSelected.station.stationId = (SELECT sSelected.stationId FROM Station sSelected WHERE sSelected.stationValue = :stationName)))) " +
       "ORDER BY pm.end_date DESC")
       List<String> findPastTrainingDates(@Param("stationName") String stationName, @Param("modelType") String modelType);

       @Query("SELECT p.modelNumber FROM PredictionModel p WHERE p.station.id = :stationId")
       List<String> findModelNumbersByStationId(int stationId);

        @Query("SELECT MAX(CAST(p.modelNumber AS int)) FROM PredictionModel p WHERE p.station.id = :stationId")
        Integer findMaxModelNumberByStationId(int stationId);
      
//        @Query("SELECT pm.end_date FROM PredictionModel pm " +
//        "WHERE pm.station = (SELECT s FROM Station s WHERE s.stationValue = :stationName) " +
//        "AND pm.modelNumber <> :modelType")
// List<String> findPastTrainingDates(@Param("stationName") String stationName, @Param("modelType") String modelType);


}