package com.smartgroceryApp.smartgrocery.common.exception;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String message) {
        super(message);
    }
}

