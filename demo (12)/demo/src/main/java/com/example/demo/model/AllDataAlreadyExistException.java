package com.example.demo.model;

public class AllDataAlreadyExistException extends ClientFaultException{
    public AllDataAlreadyExistException(String message) {
        super(message);
    }
}
