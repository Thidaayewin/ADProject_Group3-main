package com.team3.weather.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.team3.weather.model.Reference;

public interface ReferenceService {

    Reference saveReference(Reference reference);

    List<Reference> getAllReferences();

    Optional<Reference> getReferenceById(int refId);

    Reference updateReference(Reference updatedReference);

    void deleteReference(int refId);

    Map<String, List<Reference>> getDataDriftPeriods();
}

