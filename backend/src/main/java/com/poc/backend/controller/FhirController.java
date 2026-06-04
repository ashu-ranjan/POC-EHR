package com.poc.backend.controller;

import java.util.Map;

import org.hl7.fhir.r4.model.Bundle;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poc.backend.service.FhirService;

import ca.uhn.fhir.context.FhirContext;

@RestController
public class FhirController {

    private final FhirService fhirService;

    public FhirController(FhirService fhirService){
        this.fhirService = fhirService;
    }

    // Endpoint to get all resources of a specific type
    @GetMapping("/{resource}")
    public String getAll(@PathVariable String resource){

        Bundle bundle = fhirService.getAllResources(resource);

        return FhirContext.forR4()
                .newJsonParser()
                .setPrettyPrint(true)
                .encodeResourceToString(bundle);
    }

    // Endpoint to get resource by ID
    @GetMapping("/{resource}/{id}")
    public String getById(@PathVariable String resource, @PathVariable String id){

        Bundle bundle = fhirService.getById(resource, id);

        return FhirContext.forR4()
                .newJsonParser()
                .setPrettyPrint(true)
                .encodeResourceToString(bundle);
    }

    // Endpoint to search resources by parameter
    // Example: /Patient/search?name=Karla
    
    @GetMapping("/{resource}/search")
    public String searchByParam(
            @PathVariable String resource,
            @RequestParam Map<String, String> queryParams) {

        // take first param (since your service supports one)
        Map.Entry<String, String> entry = queryParams.entrySet().iterator().next();

        String param = entry.getKey();
        String value = entry.getValue();

        Bundle bundle = fhirService.searchByParam(resource, param, value);

        return FhirContext.forR4()
                .newJsonParser()
                .setPrettyPrint(true)
                .encodeResourceToString(bundle);
    }

}