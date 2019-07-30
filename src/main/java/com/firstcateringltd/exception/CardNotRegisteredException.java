package com.firstcateringltd.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CardNotRegisteredException extends RuntimeException {

    public CardNotRegisteredException() {
        this("Card for this employee not found, please provide: Employee Id, Name, email, mobile number");
    }

    public CardNotRegisteredException(String message) {
        this(message, null);
    }

    public CardNotRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }
}
