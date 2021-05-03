package com.labforward.demo.exception;

import java.util.ArrayList;
import java.util.List;

public class DefaultValidationResult implements ValidationResult {

    List<ValidationError> validationErrors = new ArrayList<>();

    @Override
    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    @Override
    public void addValidationError(ValidationError validationError) {
        validationErrors.add(validationError);
    }
}
