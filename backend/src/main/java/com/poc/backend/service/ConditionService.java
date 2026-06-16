package com.poc.backend.service;

import org.hl7.fhir.r4.model.Condition;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.ConditionEntity;
import com.poc.backend.mapper.ConditionMapper;
import com.poc.backend.repository.ConditionRepository;
import com.poc.backend.utility.IdGenerator;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class ConditionService {

    private final ConditionRepository conditionRepository;
    private final IGenericClient fhirClient;

    // Constructor Injection
    public ConditionService (ConditionRepository conditionRepository, IGenericClient fhirCLient){
        this.conditionRepository = conditionRepository;
        this.fhirClient = fhirCLient;
    }

    // CREATE CONDITION
    public ConditionEntity saveCondition(Condition condition){
        String id = condition.getIdElement().getIdPart();

        // generating identifier
        if(condition.getIdentifier().isEmpty()){
            String identifierValue = IdGenerator.generateIdentifier("CON-", 5, 5);
            condition.addIdentifier()
                        .setValue(identifierValue);
        }

        // generating full url
        String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/Condition/")
                .toUriString();
        String fullUrl = baseUrl + id;

        ConditionEntity entity = ConditionMapper.toEntity(condition, fullUrl, "match");
        return conditionRepository.save(entity);
    }

    public Condition createCondition(Condition condition){
        return (Condition) fhirClient
                            .create()
                            .resource(condition)
                            .execute()
                            .getResource();
    }

    // UPDATE CONDITION
    public Condition updateCondition(String id, Condition condition){
        condition.setId(id);
        return (Condition) fhirClient
                            .update()
                            .resource(condition)
                            .execute()
                            .getResource();
    }

}
