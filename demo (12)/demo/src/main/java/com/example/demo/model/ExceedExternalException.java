package com.example.demo.model;

public class ExceedExternalException extends ClientFaultException{
    public ExceedExternalException(String message) {
        super(message);
    }
}
