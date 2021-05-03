package com.labforward.demo.validator.valuetype;

import com.labforward.demo.entity.AttributeType;
import org.springframework.stereotype.Component;

@Component
public class StringValueTypeValidator implements ValueTypeValidator {

    @Override
    public boolean isValid(String value) {
        return true;
    }

    @Override
    public AttributeType.ValueType getValueType() {
        return AttributeType.ValueType.STRING;
    }


}
