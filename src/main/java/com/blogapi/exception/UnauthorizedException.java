package com.blogapi.exception;

public class UnauthorizedException extends RuntimeException{

    private String message;
    public UnauthorizedException() {
    }

    public UnauthorizedException(String message) {
        this.message = message;
    }
}
