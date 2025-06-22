package com.example.bcsd;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GrobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handlerMethodArgumentNotValidException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder builder = new StringBuilder();
        for(FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append(fieldError.getField())
                    .append("은 널값을 가질 수 없습니다. ");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(builder.toString());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity handlerCustomException(CustomException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

}
