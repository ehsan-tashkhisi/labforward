package com.labforward.demo.unit.validator;

import com.labforward.demo.dto.category.ItemDto;
import com.labforward.demo.entity.Attribute;
import com.labforward.demo.entity.Category;
import com.labforward.demo.exception.ItemAttributeValidationException;
import com.labforward.demo.validator.AttributeValidationManager;
import com.labforward.demo.validator.ItemValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ItemValidatorTest {

    private ItemValidator itemValidator;

    @Mock
    private AttributeValidationManager attributeValidationManager;

    @BeforeEach
    void setup() {
        itemValidator = new ItemValidator(attributeValidationManager);
    }

    @Test
    void shouldThrowExceptionWhenAttributeIsNotInCategory() {
        Category category = new Category();
        category.addAttribute(getAttribute(1, true));
        category.addAttribute(getAttribute(2, true));
        ItemDto itemDto = new ItemDto();
        itemDto.getItemAttributes().add(new ItemDto.ItemAttribute(1, "value"));
        itemDto.getItemAttributes().add(new ItemDto.ItemAttribute(2, "value"));
        itemDto.getItemAttributes().add(new ItemDto.ItemAttribute(5, "value"));
        assertThrows(ItemAttributeValidationException.class,
                () ->  itemValidator.validate(itemDto, category));
    }

    @Test
    void shouldThrowExceptionWhenRequiredAttributeIsAbsent() {
        Category category = new Category();
        category.addAttribute(getAttribute(1, true));
        category.addAttribute(getAttribute(2, false));
        ItemDto itemDto = new ItemDto();
        itemDto.getItemAttributes().add(new ItemDto.ItemAttribute(2, "value"));
        assertThrows(ItemAttributeValidationException.class,
                () ->  itemValidator.validate(itemDto, category));
    }

    private Attribute getAttribute(long id, boolean required) {
        Attribute attribute = new Attribute();
        attribute.setId(id);
        attribute.setRequired(required);
        attribute.setName("name");
        return attribute;
    }
}
