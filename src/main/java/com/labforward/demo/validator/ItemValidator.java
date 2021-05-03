package com.labforward.demo.validator;

import com.labforward.demo.dto.category.ItemDto;
import com.labforward.demo.entity.Attribute;
import com.labforward.demo.entity.Category;
import com.labforward.demo.exception.ItemAttributeValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class is responsible for validating ItemDto.
 * some constraints are relevant to the whole DTO.
 * such as when we have duplicateIds in the DTO or when we have
 * unknown attributeId in the DTO. those Constraint that are general for
 * all attributes is placed in this class but in the future we can separate
 * those validation in their own class too considering how many attribute we can
 * have for one category and some other performance constraint.
 *
 * Validation that are relevant to each attribute individually (and not the whole DTO)
 * will be investigated by {@link AttributeValidationManager} which is injected in this class
 *
 */
@Component
public class ItemValidator {

    private final AttributeValidationManager attributeValidationManager;

    @Autowired
    public ItemValidator(AttributeValidationManager attributeValidationManager) {
        this.attributeValidationManager = attributeValidationManager;
    }

    public void validate(ItemDto itemDto, Category category) {
        Map<Long, Attribute> categoryAttributes =
                category.getAttributes().stream()
                        .collect(Collectors.toMap(Attribute::getId, Function.identity()));
        checkItemDtoCategoryMismatch(itemDto, categoryAttributes);
        ValidationContext validationContext = new ValidationContext();
        itemDto.getItemAttributes().forEach(attr ->
                attributeValidationManager.validate(attr,
                        categoryAttributes.get(attr.getAttributeId()), validationContext));
        if (validationContext.isInvalid())
            throw new ItemAttributeValidationException(validationContext);
    }

    private void checkItemDtoCategoryMismatch(ItemDto itemDto, Map<Long, Attribute> categoryAttributes) {
        Map<Long, ItemDto.ItemAttribute> itemAttributesMap = new HashMap<>();
        for(ItemDto.ItemAttribute itemAttribute : itemDto.getItemAttributes()) {
            //check duplicate key
            if(itemAttributesMap.put(itemAttribute.getAttributeId(), itemAttribute) != null)
                throw new ItemAttributeValidationException(
                        itemAttribute.getAttributeId(), "Duplicate key");
        }
        checkAbsentAttributes(categoryAttributes, itemAttributesMap);
        checkUnKnownAttribute(categoryAttributes, itemAttributesMap);
    }

    private void checkUnKnownAttribute(Map<Long, Attribute> categoryAttributes,
                                       Map<Long, ItemDto.ItemAttribute> itemAttributesMap) {
        List<Long> unknownIds = itemAttributesMap.keySet()
                .stream()
                .filter(a -> !categoryAttributes.containsKey(a))
                .collect(Collectors.toList());
        if(!unknownIds.isEmpty())
            throw new ItemAttributeValidationException(unknownIds.get(0) , "Unknown attribute");
    }

    private void checkAbsentAttributes(Map<Long, Attribute> categoryAttributes,
                                            Map<Long, ItemDto.ItemAttribute> itemAttributesMap) {
        List<Long> absentAttributeIds = categoryAttributes.entrySet()
                .stream()
                .filter(entry -> entry.getValue().isRequired())
                .map(Map.Entry::getKey)
                .filter(a -> !itemAttributesMap.containsKey(a))
                .collect(Collectors.toList());
        if(!absentAttributeIds.isEmpty()) {
            Attribute attribute = categoryAttributes.get(absentAttributeIds.get(0));
            throw new ItemAttributeValidationException(attribute.getId(), "is required");
        }

    }
}
