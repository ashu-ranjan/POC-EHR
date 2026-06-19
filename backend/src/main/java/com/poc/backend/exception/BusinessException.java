package com.poc.backend.exception;

public class BusinessException extends BaseException{
    
    public BusinessException(String message){
        super(message, "BUSINESS_ERROR");
    }
}
