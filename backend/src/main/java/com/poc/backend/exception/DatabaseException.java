package com.poc.backend.exception;

public class DatabaseException extends BaseException{

    public DatabaseException(String message){
        super(message, "DB_ERROR");
    }

}
