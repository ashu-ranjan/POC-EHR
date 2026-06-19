package com.poc.backend.exception;

public class FHIRClientException extends BaseException {

    public FHIRClientException(String message){
        super(message, "FHIR_ERROR");
    }


}
