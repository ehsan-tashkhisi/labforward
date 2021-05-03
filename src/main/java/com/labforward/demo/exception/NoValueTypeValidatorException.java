package com.labforward.demo.exception;

import com.labforward.demo.entity.AttributeType;

public class NoValueTypeValidatorException extends RuntimeException {

    AttributeType.ValueType valueType;

    public NoValueTypeValidatorException(AttributeType.ValueType valueType) {
        this.valueType = valueType;
    }
}
