package com.musalasoft.gateway.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =new ExceptionResponse(new Date(), ex.getMessage(),
        request.getDescription(false),"Resource not found");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }

    public final ResponseEntity<Object> handleGatewayNotFoundException(GatewayNotFoundExeption gx, WebRequest request){
        ExceptionResponse exceptionResponse =new ExceptionResponse(new Date(), gx.getMessage(),
        request.getDescription(false),"Gateway not found");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }

    public final ResponseEntity<Object> handlePeripheralNotFoundException(PeripheralNotFoundException px, WebRequest request){
        ExceptionResponse exceptionResponse =new ExceptionResponse(new Date(), px.getMessage(),
        request.getDescription(false),"Peripheral not found");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }

    public final ResponseEntity<Object> handleGatewayExceptions(GatewayException gx, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), gx.getMessage(),
            request.getDescription(false),"Nothing to see here");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
      }
}
