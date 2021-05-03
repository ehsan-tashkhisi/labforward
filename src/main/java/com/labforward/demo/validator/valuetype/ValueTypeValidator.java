package com.labforward.demo.validator.valuetype;

import com.labforward.demo.entity.AttributeType;

public interface ValueTypeValidator {

    boolean isValid(String value);
    AttributeType.ValueType getValueType();
}
