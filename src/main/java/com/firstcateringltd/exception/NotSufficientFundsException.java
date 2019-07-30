package com.firstcateringltd.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotSufficientFundsException extends RuntimeException {

    public NotSufficientFundsException() {
        this("There are not sufficient funds to make a withdrawal of that size");
    }

    public NotSufficientFundsException(String message) {
        this(message, null);
    }

    public NotSufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }
}
