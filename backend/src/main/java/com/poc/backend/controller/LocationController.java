package com.poc.backend.controller;

import org.hl7.fhir.r4.model.Location;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.poc.backend.service.LocationService;

import ca.uhn.fhir.context.FhirContext;

@RestController
public class LocationController {

    private final FhirContext context = FhirContext.forR4();
    private final LocationService locationService;

    public LocationController(LocationService locationService){
        this.locationService = locationService;
    }

    // Create API

    @PostMapping("/Location")
    public String create(@RequestBody String body){
        Location location = (Location) context
                                    .newJsonParser()
                                    .parseResource(body);
        // Create in FHIR
        Location created = locationService.createLocation(location);

        // Save create in DB
        locationService.saveLocation(created);

        return context.newJsonParser()
                    .setPrettyPrint(true)
                    .encodeResourceToString(created);
    }

    // Update API

    @PutMapping("/Location/{id}")
    public String update(@PathVariable String id, @RequestBody String body){
        Location location = (Location) context
                                    .newJsonParser()
                                    .parseResource(body);
                           
        // update in FHIR
        Location updated = locationService.updateLocation(id, location);
        
        // Update location in DB
        locationService.saveLocation(updated);

        return context.newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(updated);
    }

}
