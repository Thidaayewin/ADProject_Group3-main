package com.team3.weather.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.team3.weather.model.PredictionModel;
import com.team3.weather.model.Rainfall;
import com.team3.weather.model.RainfallStatistics;
import com.team3.weather.model.Reference;
import com.team3.weather.model.Station;

@Repository
public interface RainfallRepository extends JpaRepository<Rainfall, Integer> {
    // Add custom query methods if needed

    // @Query(value = "select new com.team3.caps.model.RainfallStatistics(DATE_FORMAT(rainfallTime, ?4),sum(rainfallValue)) " +
    //         "from Rainfall where DATE_FORMAT(rainfallTime, ?4) between DATE_FORMAT(?1, ?4) " +
    //         "and DATE_FORMAT(?2, ?4) group by DATE_FORMAT(rainfallTime, ?4)")
    // List<RainfallStatistics> findRainfallInfo(String startTime, String endTime, String format);
    // @Query("SELECT NEW com.team3.caps.model.RainfallStatistics(FUNCTION('FORMATDATETIME', r.timestamp, ?3), SUM(r.actualRainfallAmount)) " +
    //        "FROM Rainfall r " +
    //        "WHERE r.timestamp BETWEEN FUNCTION('FORMATDATETIME', ?1, ?3) AND FUNCTION('FORMATDATETIME', ?2, ?3) " +
    //         "AND station=?4" +
    //        "GROUP BY FUNCTION('FORMATDATETIME', r.timestamp, ?3)")
    // List<RainfallStatistics> findRainfallInfo(LocalDateTime startTime, LocalDateTime endTime, String format, String stationId);
    //List<RainfallStatistics> findRainfallInfo(LocalDateTime startTime, LocalDateTime endTime, String format);

    // Find rainfall data filtered by researcher
    // @Query("SELECT NEW com.team3.caps.model.RainfallStatistics(FUNCTION('FORMATDATETIME', r.timestamp, ?3), SUM(r.actualRainfallAmount)) " +
    //    "FROM Rainfall r " +
    //    "WHERE r.timestamp BETWEEN FUNCTION('FORMATDATETIME', ?1, ?3) AND FUNCTION('FORMATDATETIME', ?2, ?3) " +
    //    "AND r.station = ?4 " +
    //    "GROUP BY FUNCTION('FORMATDATETIME', r.timestamp, ?3)")
    // List<RainfallStatistics> findRainfallQuery(LocalDateTime start, LocalDateTime end, PredictionModel predModel, Station station);

    @Query("SELECT r FROM Rainfall r WHERE r.station.stationId = ?1")
    List<Rainfall> findByStationId(int stationId);

    @Query("SELECT r FROM Rainfall r WHERE r.station.stationId = ?1 ORDER BY r.timestamp DESC")
    List<Rainfall> findMostRecent12MonthsByStationId(int stationId);

    // @Query(value = "SELECT r.* FROM Rainfall r WHERE r.station_id = ?1 ORDER BY r.timestamp DESC LIMIT 12", nativeQuery = true)
    // List<Rainfall> findMostRecent12MonthsByStationId(int stationId);

    
}
