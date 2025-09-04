package com.example.autokolcsonzes.Utils;

import java.util.List;

public class ValidationException extends RuntimeException {
    private final List<String> errors;

    public ValidationException(List<String> errors) {
        super(String.join("; ", errors)); // combined message
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}

