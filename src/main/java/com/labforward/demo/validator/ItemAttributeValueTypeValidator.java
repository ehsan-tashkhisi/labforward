package com.labforward.demo.validator;

import com.labforward.demo.dto.category.ItemDto;
import com.labforward.demo.entity.Attribute;
import com.labforward.demo.entity.AttributeType;
import com.labforward.demo.exception.NoValueTypeValidatorException;
import com.labforward.demo.validator.valuetype.ValueTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;

/**
 * this class is responsible for validating each attribute based on their valueTypes.
 * this generate a registry for validators and apply them on attribute based
 * on their valueTypes. Each time we define new ValueType in our system
 * we must define associated {@link ValueTypeValidator} for that specific
 * type otherwise {@link NoValueTypeValidatorException} exception will be thrown.
 */
@Component
public class ItemAttributeValueTypeValidator implements ItemAttributeValidator {

    private final EnumMap<AttributeType.ValueType, ValueTypeValidator> valueTypeValidatorRegistry =
            new EnumMap<>(AttributeType.ValueType.class);

    @Autowired
    public ItemAttributeValueTypeValidator(List<ValueTypeValidator> valueTypeValidators) {
        valueTypeValidators.forEach(validator ->
                valueTypeValidatorRegistry.put(validator.getValueType(), validator));
    }

    @Override
    public void validate(ItemDto.ItemAttribute itemAttribute, Attribute attribute,
                         ValidationContext validationContext) {
        AttributeType.ValueType valueType = attribute.getAttributeType().getValueType();
        ValueTypeValidator validator = valueTypeValidatorRegistry
                .get(valueType);
            if(validator == null)
                throw new NoValueTypeValidatorException(valueType);
            if(!validator.isValid(itemAttribute.getValue())) {
                validationContext.addValidationError(
                        new ValidationContext.ValidationError(
                                String.valueOf(attribute.getId()),
                                itemAttribute.getValue(), "is not " +
                                validator.getValueType()));
                validationContext.setInvalid();
            }
    }
}
