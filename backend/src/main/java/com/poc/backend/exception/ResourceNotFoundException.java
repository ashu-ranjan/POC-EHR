package com.poc.backend.exception;

public class ResourceNotFoundException extends BaseException{

    public ResourceNotFoundException(String message){
        super(message, "NOT_FOUND");
    }

}
