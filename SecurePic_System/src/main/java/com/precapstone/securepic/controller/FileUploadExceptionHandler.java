package com.precapstone.securepic.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class FileUploadExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return "File size exceeds the allowable limit (20MB). Please upload a smaller file.";
    }
}