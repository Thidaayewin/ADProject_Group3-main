package com.team3.weather.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team3.weather.model.Reference;
import com.team3.weather.repository.ReferenceRepository;
import com.team3.weather.service.ReferenceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReferenceServiceImpl implements ReferenceService {

    @Autowired
    private final ReferenceRepository referenceRepository;

    
    public ReferenceServiceImpl(ReferenceRepository referenceRepository) {
        this.referenceRepository = referenceRepository;
    }

    @Override
    public Reference saveReference(Reference reference) {
        return referenceRepository.save(reference);
    }

    @Override
    public List<Reference> getAllReferences() {
        return referenceRepository.findAll();
    }

    @Override
    public Optional<Reference> getReferenceById(int refId) {
        return referenceRepository.findById(refId);
    }

    @Override
    public Reference updateReference(Reference updatedReference) {
        return referenceRepository.save(updatedReference);
    }

    @Override
    public void deleteReference(int refId) {
        referenceRepository.deleteById(refId);
    }

    // @Override
    // public Map<String, List<String>> getDataDriftPeriods() {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'getDataDriftPeriods'");
    // }
     @Override
    //for elnino la nina
    public Map<String,List<Reference>> getDataDriftPeriods(){
        List<Reference> elNino= referenceRepository.findByType("ElNino");
        List<Reference> laNina=referenceRepository.findByType("LaNina");

        Map<String,List<Reference>> dataDriftPeriods = new HashMap<>();
        dataDriftPeriods.put("El Nino",elNino);
        dataDriftPeriods.put("La Nina",laNina);
        
        return dataDriftPeriods;
    }
}

