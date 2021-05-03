package com.labforward.demo.validator.valuetype;

import com.labforward.demo.entity.AttributeType;
import org.springframework.stereotype.Component;

@Component
public class DoubleValueTypeValidator implements ValueTypeValidator {

    @Override
    public boolean isValid(String value) {
        try{
            Double.valueOf(value);
        }catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    @Override
    public AttributeType.ValueType getValueType() {
        return AttributeType.ValueType.DOUBLE;
    }

}
