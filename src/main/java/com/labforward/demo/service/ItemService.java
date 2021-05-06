package com.labforward.demo.service;

import com.labforward.demo.dto.category.ItemDto;
import com.labforward.demo.entity.Attribute;
import com.labforward.demo.entity.Category;
import com.labforward.demo.entity.Item;
import com.labforward.demo.entity.ItemAttribute;
import com.labforward.demo.exception.ResourceNotFoundException;
import com.labforward.demo.repository.ItemRepository;
import com.labforward.demo.validator.ItemValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class provides a simple implementation for saving ang updating Items.
 * for validating DTO there are many constraints that should be considered.
 * {@link ItemValidator} is injected in this class for that purposes. If DTO is validated
 * all attributes in that DTO should be saved, and all previous ones should be removed
 * at this time no PATCH like operation in supported. but we can add those operation
 * with ease but only when all other constraint is considered.
 */
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @Autowired
    public ItemService(ItemRepository itemRepository, ItemValidator itemValidator) {
        this.itemRepository = itemRepository;
        this.itemValidator = itemValidator;
    }

    public Item createItem(Category category, ItemDto itemDto) {
        sanitizeDto(itemDto);
        itemValidator.validate(itemDto, category);
        Map<Long, Attribute> categoryAttributes =
                getCategoryAttributesMap(category);
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setCategory(category);
        itemDto.getItemAttributes().forEach(itemAttributeDto -> {
            ItemAttribute itemAttribute = new ItemAttribute();
            itemAttribute.setValue(itemAttributeDto.getValue());
            itemAttribute.setAttribute(categoryAttributes.get(itemAttributeDto.getAttributeId()));
            item.addItemAttribute(itemAttribute);
        });
        return itemRepository.save(item);
    }

    private void sanitizeDto(ItemDto itemDto) {
        itemDto.getItemAttributes()
                .removeIf(itemAttribute -> itemAttribute.getValue() == null);
    }

    @Transactional
    public Item updateItem(long id, ItemDto itemDto) {
        Item item = getItem(id);
        sanitizeDto(itemDto);
        itemValidator.validate(itemDto, item.getCategory());
        item.setName(itemDto.getName());
        Map<Long, Attribute> categoryAttributes = getCategoryAttributesMap(item.getCategory());
        Map<Long, ItemAttribute> itemAttributes = getItemAttributesMap(item);
        List<ItemAttribute> newAttributes = new ArrayList<>();
        itemDto.getItemAttributes().forEach(a -> {
            ItemAttribute itemAttribute = itemAttributes.getOrDefault(a.getAttributeId(), new ItemAttribute());
            itemAttribute.setValue(a.getValue());
            itemAttribute.setAttribute(categoryAttributes.get(a.getAttributeId()));
            newAttributes.add(itemAttribute);
        });
        itemAttributes.values().forEach(item::removeItemAttribute);
        newAttributes.forEach(item::addItemAttribute);
        return item;
    }

    private Map<Long, ItemAttribute> getItemAttributesMap(Item item) {
        return item.getItemAttributes().stream()
                .collect(Collectors.toMap(a -> a.getAttribute().getId(),
                        Function.identity()));
    }

    private Item getItem(@PathVariable long id) {
        return itemRepository.findItemByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }

    private Map<Long, Attribute> getCategoryAttributesMap(Category category) {
        return category.getAttributes().stream()
                .collect(Collectors.toMap(Attribute::getId,
                        Function.identity()));
    }
}
