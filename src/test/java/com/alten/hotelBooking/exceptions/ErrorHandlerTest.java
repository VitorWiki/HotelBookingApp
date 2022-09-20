package com.alten.hotelBooking.exceptions;

import com.alten.hotelBooking.exceptions.handlers.ErrorHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

public class ErrorHandlerTest {

    ErrorHandler handler = new ErrorHandler();

    @Test
    public void must_handle_runtimeException() {

       ResponseEntity<ErrorResponse> runtimeCatcher = handler.handleRuntimeException(new RuntimeException("runtimeError"));

        Assertions.assertEquals(runtimeCatcher.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        Assertions.assertEquals(runtimeCatcher.getBody().getStatus(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        Assertions.assertEquals(runtimeCatcher.getBody().getDescription(), "runtimeError");
    }

    @Test
    public void must_handle_responseStatusException() {

        ResponseEntity<ErrorResponse> runtimeCatcher = handler.handleResponseStatusException(new ResponseStatusException(HttpStatus.NOT_FOUND, "statusError"));

        Assertions.assertEquals(runtimeCatcher.getStatusCode(), HttpStatus.NOT_FOUND);
        Assertions.assertEquals(runtimeCatcher.getBody().getStatus(), HttpStatus.NOT_FOUND.getReasonPhrase());
    }

}
