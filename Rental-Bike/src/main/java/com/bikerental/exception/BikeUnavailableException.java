package com.bikerental.exception;

public class BikeUnavailableException extends RuntimeException {
    public BikeUnavailableException(String message) {
        super(message);
    }
}