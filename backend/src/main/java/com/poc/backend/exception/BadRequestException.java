package com.poc.backend.exception;

public class BadRequestException extends BaseException{

    public BadRequestException(String message){
        super(message, "BAD_REQUEST");
    }

}
