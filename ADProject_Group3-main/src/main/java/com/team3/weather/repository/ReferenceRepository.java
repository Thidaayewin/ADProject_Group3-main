package com.team3.weather.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.team3.weather.model.Reference;

@Repository
public interface ReferenceRepository extends JpaRepository<Reference, Integer> {
    // You can add custom query methods here if needed
    
    //Get El nino/La nina data 
    //@query --> get month from reference if type = type
    // get back list of all the months where it is elnino/lanina  
//     @Query("SELECT r.monthly FROM References r WHERE r.TYPE = :type")
//     List<String> findDataDriftPeriods(@Param("type")String type);

        List<Reference> findByType(String type);
 }
