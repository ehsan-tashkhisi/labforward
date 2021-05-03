package com.labforward.demo.validator.valuetype;

import com.labforward.demo.entity.AttributeType;
import org.springframework.stereotype.Component;

@Component
public class IntegerValueTypeValidator implements ValueTypeValidator {

    @Override
    public boolean isValid(String value) {
        try{
            Integer.valueOf(value);
        }catch (NumberFormatException ex) {
           return false;
        }
        return true;
    }

    @Override
    public AttributeType.ValueType getValueType() {
        return AttributeType.ValueType.INTEGER;
    }

}
