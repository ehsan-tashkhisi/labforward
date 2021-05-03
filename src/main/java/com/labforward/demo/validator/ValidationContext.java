package com.labforward.demo.validator;

import com.labforward.demo.exception.ValidationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidationContext implements ValidationResult {

    private final List<ValidationError> errors = new ArrayList<>();

    private boolean invalid = false;

    public List<ValidationError> getValidationErrors() {
        return Collections.unmodifiableList(errors);
    }

    public void addValidationError(ValidationError validationError) {
        errors.add(validationError);
    }

    boolean isInvalid() {
        return invalid;
    }

    void setInvalid() {
        this.invalid = true;
    }
}



