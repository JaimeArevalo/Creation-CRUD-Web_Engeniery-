package com.carddemo.exception;

import java.util.ArrayList;
import java.util.List;

public class InvalidCardDataException extends RuntimeException {
    private List<String> errors;

    public InvalidCardDataException(String message) {
        super(message);
        this.errors = new ArrayList<>();
        this.errors.add(message);
    }

    public InvalidCardDataException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}