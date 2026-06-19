package com.poc.backend.controller;

import org.hl7.fhir.r4.model.Location;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poc.backend.service.LocationService;

import ca.uhn.fhir.context.FhirContext;

@RestController
@RequestMapping("/Location")
public class LocationController {

    private final FhirContext context = FhirContext.forR4();
    private final LocationService service;

    public LocationController(LocationService service){
        this.service = service;
    }

    // Create API 

    @PostMapping
    public ResponseEntity<String> create(@RequestBody String body){

        Location location = (Location) context
                                    .newJsonParser()
                                    .parseResource(body);

        Location created = service.create(location);

        service.save(created);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(context.newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(created));
    }

    // Update API

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable String id,
                                         @RequestBody String body){

        Location location = (Location) context
                                    .newJsonParser()
                                    .parseResource(body);

        location.setId(id);

        Location updated = service.update(id, location);

        service.save(updated);

        return ResponseEntity.ok(
                context.newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(updated));
    }
}
