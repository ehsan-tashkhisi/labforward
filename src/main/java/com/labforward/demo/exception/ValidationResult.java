package com.labforward.demo.exception;

import lombok.Data;

import java.util.List;

public interface ValidationResult {

    List<ValidationError> getValidationErrors();

    void addValidationError(ValidationError validationError);

    @Data
    class ValidationError {
        private final String source;
        private final String value;
        private final String message;
    }
}
