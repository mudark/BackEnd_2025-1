package com.example.bcsd;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    private final HttpStatus httpStatus; // Http 상태값

    public CustomException(HttpStatus h, String message) {
        super(message);
        httpStatus=h;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
