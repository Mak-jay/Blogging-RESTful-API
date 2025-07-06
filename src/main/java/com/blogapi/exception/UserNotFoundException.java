package com.blogapi.exception;

public class UserNotFoundException extends RuntimeException{

    private String message;

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        this.message = message;
    }

    public UserNotFoundException(String s, Object o, String s1) {
    }
}
