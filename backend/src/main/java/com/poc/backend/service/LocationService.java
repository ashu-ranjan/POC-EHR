package com.poc.backend.service;

import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Location;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.LocationEntity;
import com.poc.backend.exception.BadRequestException;
import com.poc.backend.exception.DatabaseException;
import com.poc.backend.exception.FHIRClientException;
import com.poc.backend.mapper.LocationMapper;
import com.poc.backend.repository.LocationRepository;
import com.poc.backend.utility.IdGenerator;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class LocationService {

    private final LocationRepository repository;
    private final IGenericClient fhirClient;

    public LocationService(LocationRepository repository, IGenericClient fhirClient) {
        this.repository = repository;
        this.fhirClient = fhirClient;
    }

    // CREATE LOCATION

    // Create Location to FHIR
    public Location create(Location location){

        // validation
        if (!location.hasName()) {
            throw new BadRequestException("Location name is required.");
        }

        // identifier generation
        if(location.getIdentifier().isEmpty()){
            Identifier identifier = new Identifier();
            identifier.setSystem("http://localIdentifier.de/identifiers/location");
            identifier.setValue(IdGenerator.generateIdentifier("LOC-", 4, 4));
            location.addIdentifier(identifier);
        }

        // core creation
        try {
            return (Location) fhirClient
                    .create()
                    .resource(location)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to create Location.");
        }
    }

    // Save Location to DB
    public LocationEntity save(Location location){

        try {
            String id = location.getIdElement().getIdPart();

            String fullUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/Location/")
                    .toUriString() + id;

            return repository.save(
                    LocationMapper.toEntity(location, fullUrl, "match"));

        } catch (Exception e) {
            throw new DatabaseException("Failed to save Location.");
        }
    }

    // UPDATE LOCATION
    
    public Location update(String id, Location location){

        // validation
        if(id == null || id.isEmpty()){
            throw new BadRequestException("ID is required for update.");
        }

        // core updation
        try {

            Location existing =
                    (Location) fhirClient
                            .read()
                            .resource(Location.class)
                            .withId(id)
                            .execute();

            // ALWAYS preserve identifier
            location.setIdentifier(existing.getIdentifier());

            location.setId(id);

            return (Location) fhirClient
                    .update()
                    .resource(location)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to update Location.");
        }
    }
}