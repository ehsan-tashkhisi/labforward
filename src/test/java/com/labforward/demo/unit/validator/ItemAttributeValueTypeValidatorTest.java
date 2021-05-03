package com.labforward.demo.unit.validator;

import com.labforward.demo.dto.category.ItemDto;
import com.labforward.demo.entity.Attribute;
import com.labforward.demo.entity.AttributeType;
import com.labforward.demo.exception.NoValueTypeValidatorException;
import com.labforward.demo.validator.ItemAttributeValueTypeValidator;
import com.labforward.demo.validator.ValidationContext;
import com.labforward.demo.validator.valuetype.ValueTypeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemAttributeValueTypeValidatorTest {

    @Mock
    private ValueTypeValidator doubleValueTypeValidator;

    @Mock
    private ValueTypeValidator stringValueTypeValidator;

    private ItemAttributeValueTypeValidator itemAttributeValueTypeValidator;

    @BeforeEach
    void setup() {
        when(doubleValueTypeValidator.getValueType()).thenReturn(AttributeType.ValueType.DOUBLE);
        when(stringValueTypeValidator.getValueType()).thenReturn(AttributeType.ValueType.STRING);
        itemAttributeValueTypeValidator =
                new ItemAttributeValueTypeValidator(Arrays.asList(doubleValueTypeValidator, stringValueTypeValidator));
    }

    @Test
    void shouldThrowExceptionWhenValueTypeValidatorDoesNotExist() {
        Attribute attribute = new Attribute();
        AttributeType attributeType = new AttributeType();
        attributeType.setValueType(AttributeType.ValueType.INTEGER);
        attribute.setAttributeType(attributeType);
        ItemDto.ItemAttribute itemAttribute = new ItemDto.ItemAttribute();
        ValidationContext validationContext = new ValidationContext();
        assertThrows(NoValueTypeValidatorException.class,
                () ->  itemAttributeValueTypeValidator.validate(itemAttribute, attribute, validationContext));
    }

    @Test
    void shouldCallAssociatedValueTypeValidator() {
        Attribute attribute = new Attribute();
        AttributeType attributeType = new AttributeType();
        attributeType.setValueType(AttributeType.ValueType.STRING);
        attribute.setAttributeType(attributeType);
        ItemDto.ItemAttribute itemAttribute = new ItemDto.ItemAttribute();
        itemAttribute.setValue("test");
        ValidationContext validationContext = new ValidationContext();
        itemAttributeValueTypeValidator.validate(itemAttribute, attribute, validationContext);
        verify(stringValueTypeValidator).isValid(itemAttribute.getValue());
    }

}
