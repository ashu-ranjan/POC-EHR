package com.poc.backend.service;

import org.hl7.fhir.r4.model.Bundle;
import org.springframework.stereotype.Service;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.StringClientParam;

@Service
public class FhirService {

    private final IGenericClient client;

    // Constructor injection of the FHIR client
    public FhirService(IGenericClient client){
        this.client = client;
    }

    // Get all resources generic method
    public Bundle getAllResources(String resourceType){
        return client
            .search()
            .forResource(resourceType)
            .returnBundle(Bundle.class)
            .execute();
    }


    // Get resource by ID generic method
    public Bundle getById(String resourceType, String id) {
        return client
                .search()
                .forResource(resourceType)
                .where(new StringClientParam("_id").matches().value(id))
                .returnBundle(Bundle.class)
                .execute();
    }

    // Search resources by parameter generic method
    public Bundle searchByParam(String resourceType, String param, String value) {
        return client
                .search()
                .forResource(resourceType)
                .where(new StringClientParam(param).matches().value(value))
                .returnBundle(Bundle.class)
                .execute();
    }


}
