package com.blogapi.exception;

public class ResourceNotFoundException extends RuntimeException{
    private String message;

    public ResourceNotFoundException() {
    }
    public ResourceNotFoundException(String message) {
        this.message = message;
    }
}
