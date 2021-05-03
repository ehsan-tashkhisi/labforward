package com.labforward.demo.exception;

public class ItemAttributeValidationException extends LabForwardValidationException {

    public ItemAttributeValidationException(ValidationResult validationResult) {
        super(validationResult);
    }

    public ItemAttributeValidationException(Long id, String message) {
        super("attributeId", String.valueOf(id), message);
    }
}
