package com.labforward.demo.unit.validator;

import com.labforward.demo.dto.category.ItemDto;
import com.labforward.demo.entity.Attribute;
import com.labforward.demo.validator.AttributeValidationManager;
import com.labforward.demo.validator.ItemAttributeValidator;
import com.labforward.demo.validator.ValidationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AttributeValidationManagerTest {

    @Mock
    private ItemAttributeValidator itemAttributeValidator;

    private AttributeValidationManager attributeValidationManager;

    @BeforeEach
    void setup() {
        attributeValidationManager =
                new AttributeValidationManager(Collections.singletonList(itemAttributeValidator));
    }

    @Test
    void shouldCallItemAttributeValidateWhenValidate() {
        Attribute attribute = new Attribute();
        ItemDto.ItemAttribute itemAttributeDto = new ItemDto.ItemAttribute();
        ValidationContext validationContext = new ValidationContext();
        attributeValidationManager.validate(itemAttributeDto, attribute, validationContext);
        verify(itemAttributeValidator).validate(itemAttributeDto, attribute, validationContext);
    }

}
