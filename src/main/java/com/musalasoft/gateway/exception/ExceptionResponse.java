package com.musalasoft.gateway.exception;

import java.util.Date;

import lombok.Data;

@Data
public class ExceptionResponse {
    private Date date;
    private String message;
    private String details;
    private String trace;

    public ExceptionResponse(Date timestamp, String message, String details, String trace) {
        super();
        this.date = timestamp;
        this.message = message;
        this.details = details;
        this.trace = trace;
    }
}