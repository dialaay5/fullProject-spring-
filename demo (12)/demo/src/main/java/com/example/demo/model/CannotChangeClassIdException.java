package com.example.demo.model;

public class CannotChangeClassIdException extends ClientFaultException{
    public CannotChangeClassIdException(String message) {
        super(message);
    }
}
