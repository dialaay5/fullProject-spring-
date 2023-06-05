package com.example.demo.model;

public class ClassNotEmptyException extends ClientFaultException{

    public ClassNotEmptyException(String message) {
        super(message);
    }
}
