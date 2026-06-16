package com.poc.backend.controller;


import org.hl7.fhir.r4.model.Condition;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.poc.backend.service.ConditionService;

import ca.uhn.fhir.context.FhirContext;

@RestController
public class ConditionController {

    private final ConditionService conditionService;
    private final FhirContext context = FhirContext.forR4();

    // Constructor Injection
    public ConditionController(ConditionService conditionService){
        this.conditionService = conditionService;
    }

    // Create API
    @PostMapping("/Condition")
    public String create(@RequestBody String body){
        Condition condition = (Condition) context
                                    .newJsonParser()
                                    .parseResource(body);

        // Create in FHIR
        Condition created = conditionService.createCondition(condition);

        // Save create in DB
        conditionService.saveCondition(created);

        return context.newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(created);
    }

    // Update API
    @PutMapping("/Condition/{id}")
    public String update(@PathVariable String id, @RequestBody String body){
        Condition condition = (Condition) context
                                    .newJsonParser()
                                    .parseResource(body);

        // Update in FHIR
        Condition updated = conditionService.updateCondition(id, condition);

        // Save update in DB
        conditionService.saveCondition(condition);

        return context.newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(updated);
    }

}
