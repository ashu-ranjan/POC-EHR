package com.poc.backend.service;

import org.hl7.fhir.r4.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.LocationEntity;
import com.poc.backend.mapper.LocationMapper;
import com.poc.backend.repository.LocationRepository;
import com.poc.backend.utility.IdGenerator;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private IGenericClient fhirClient;


    // Create Location

    public LocationEntity saveLocation(Location location){
        String id = location.getIdElement().getIdPart();

        if(location.getIdentifier().isEmpty()){
            String identifierValue = IdGenerator.generateLocIdentifier("LOC-", 4,4);
            location.addIdentifier()
                        .setSystem("http://localIdentifier.de/identifiers/location")
                        .setValue(identifierValue);
        }

        String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/Location/")
                .toUriString();
        String fullUrl = baseUrl + id;

        LocationEntity entity = LocationMapper.toEntity(location, fullUrl, "match");

        return locationRepository.save(entity);
    }

    public Location createLocation(Location location){

        // location.setIdElement(null); When needed active this

        return (Location) fhirClient
                        .create()
                        .resource(location)
                        .execute()
                        .getResource();
    }

    // Update Location 

    public Location updateLocation(String id, Location location){
        location.setId(id);

        return (Location) fhirClient
                        .update()
                        .resource(location)
                        .execute()
                        .getResource();
    }

}
