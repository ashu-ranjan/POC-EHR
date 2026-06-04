package com.poc.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;

@Configuration
public class FhirConfig {

    private static final String SERVER_BASE = "http://hapi.fhir.org/baseR4";

    @Bean
    public FhirContext fhirContext(){
        FhirContext ctx = FhirContext.forR4();
        ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
        return ctx;
    }

    @Bean
    public IGenericClient fhirClient(FhirContext ctx){
        return ctx.newRestfulGenericClient(SERVER_BASE);
    }

}
