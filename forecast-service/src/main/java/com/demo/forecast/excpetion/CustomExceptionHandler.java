package com.demo.forecast.excpetion;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Server Error", details);
        return handleExceptionInternal(ex, error, generateHeaders(), INTERNAL_SERVER_ERROR, request);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DataNotFoundException.class)
    public final ResponseEntity<Object> handleRecordNotFoundException(DataNotFoundException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse("Data Not Found", List.of(ex.getLocalizedMessage()));
        return ResponseEntity.status(NOT_FOUND).body(error);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status,
                                                                         WebRequest request) {
        return ResponseEntity.status(UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse(String.format("Not Supported method: %s", ex.getMethod()),
                        List.of("Supported methods are: ",
                                String.join(",", Objects.requireNonNull(ex.getSupportedMethods())))));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.status(UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse("Parameter missing or not valid",
                        List.of("Parameter must be:",
                                String.format("Type: %s, name: %s", ex.getParameterType(), ex.getParameterName()))));
    }


    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
