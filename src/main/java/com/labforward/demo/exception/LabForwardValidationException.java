package com.labforward.demo.exception;

import com.labforward.demo.validator.ValidationContext;

import java.util.List;

public class LabForwardValidationException extends LabForwardException {

    private final ValidationResult validationResult;

    public LabForwardValidationException(ValidationResult validationResult) {
        this.validationResult = validationResult;
    }

    public LabForwardValidationException(String source, String value, String message) {
        this.validationResult = new DefaultValidationResult();
        validationResult.addValidationError(
                new ValidationResult.ValidationError(source, value, message));
    }

    public List<ValidationContext.ValidationError> getAllErrors() {
        return this.validationResult.getValidationErrors();
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        validationResult.getValidationErrors().forEach(error ->
                sb.append("source: ")
                        .append(error.getSource())
                        .append(" value: ")
                        .append(error.getValue())
                        .append(" ")
                        .append(error.getMessage()));
        return sb.toString();
    }
}
