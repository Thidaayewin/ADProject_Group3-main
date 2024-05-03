package com.team3.weather.exception;

public class BadTagException extends Exception{
    public BadTagException() {
        super();
    }

    public BadTagException(String messsage) {
        super(messsage);
    }
}
