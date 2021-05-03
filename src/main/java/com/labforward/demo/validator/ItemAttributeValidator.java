package com.labforward.demo.validator;

import com.labforward.demo.dto.category.ItemDto;
import com.labforward.demo.entity.Attribute;

/**
 * this interface has single validate method witch validate
 * {@link ItemDto.ItemAttribute} bases on some constraint.
 */
public interface ItemAttributeValidator {

    void validate(ItemDto.ItemAttribute itemAttribute, Attribute attribute, ValidationContext validationContext);
}
