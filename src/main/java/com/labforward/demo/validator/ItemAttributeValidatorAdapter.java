package com.labforward.demo.validator;

import com.labforward.demo.dto.category.ItemDto;
import com.labforward.demo.entity.Attribute;

public abstract class ItemAttributeValidatorAdapter implements ItemAttributeValidator {

    @Override
    public void validate(ItemDto.ItemAttribute itemAttribute, Attribute attribute, ValidationContext validationContext) {
        if(!isValid(itemAttribute)) {
            validationContext.setInvalid();
            validationContext.addValidationError(
                    new ValidationContext.ValidationError(
                            attribute.getName(),
                            itemAttribute.getValue(),
                            getMessage()));
        }
    }

    abstract boolean isValid(ItemDto.ItemAttribute itemAttribute);

    abstract String getMessage();
}
