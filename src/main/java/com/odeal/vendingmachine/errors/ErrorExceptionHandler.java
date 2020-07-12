package com.odeal.vendingmachine.errors;

import com.odeal.vendingmachine.orders.controllers.responses.OrderResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
@Log4j2
public class ErrorExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {
            ErrorRuntimeException.class
    })
    protected ResponseEntity<Object> handleConflict(
            ErrorRuntimeException ex, WebRequest request) {
        log.error(ex);
        OrderResponse response = OrderResponse.builder()
                .changes(ex.getMoneyList())
                .errorMessage(ex.getMessage())
                .build();
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    @ExceptionHandler(value
            = {
            Exception.class
    })
    protected ResponseEntity<Object> exception(
            Exception ex, WebRequest request) {
        log.error(ex);
        OrderResponse response = OrderResponse.builder()
                .errorMessage("Internal Server Error")
                .build();
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> validationList = ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        return handleExceptionInternal(ex, validationList, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}
