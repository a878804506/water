package com.cyh.exception;

@SuppressWarnings("serial")
public class MyException extends Exception {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MyException() {
    }

    public MyException(String message) {
        super(message);
        this.message = message;
    }

}
