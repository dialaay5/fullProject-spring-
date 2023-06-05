package com.example.demo.model;

public class NotIllegalChangeClassType extends ClientFaultException{
    public NotIllegalChangeClassType(String message) {
        super(message);
    }
}
