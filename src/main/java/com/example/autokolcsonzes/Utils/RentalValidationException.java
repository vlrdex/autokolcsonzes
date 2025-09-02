package com.example.autokolcsonzes.Utils;

import java.util.List;

public class RentalValidationException extends RuntimeException {
    private final List<String> errors;

    public RentalValidationException(List<String> errors) {
        super(String.join("; ", errors)); // combined message
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}

