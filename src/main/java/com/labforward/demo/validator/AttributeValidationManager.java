package com.labforward.demo.validator;

import com.labforward.demo.dto.category.ItemDto;
import com.labforward.demo.entity.Attribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class is just a manager delegating attribute to {@link ItemAttributeValidator}
 * for validation. this class call all ItemValidators that are injected to it
 * to do some validation on the attribute.
 */
@Component
public class AttributeValidationManager {

    private final List<ItemAttributeValidator> validatorList;

    @Autowired
    public AttributeValidationManager(List<ItemAttributeValidator> validatorList) {
        this.validatorList = validatorList;
    }

    public void validate(ItemDto.ItemAttribute attr,
                         Attribute attribute, ValidationContext validationContext) {
        validatorList.forEach(v -> v.validate(attr, attribute, validationContext));
    }
}
