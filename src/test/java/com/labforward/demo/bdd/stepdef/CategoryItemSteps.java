package com.labforward.demo.bdd.stepdef;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.labforward.demo.bdd.exception.InvalidStateException;
import com.labforward.demo.dto.category.AttributeDto;
import com.labforward.demo.dto.category.ItemDto;
import com.labforward.demo.entity.*;
import com.labforward.demo.repository.AttributeRepository;
import com.labforward.demo.repository.AttributeTypeRepository;
import com.labforward.demo.repository.CategoryRepository;
import com.labforward.demo.repository.ItemRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CategoryItemSteps extends AbstractSteps implements En {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private AttributeTypeRepository attributeTypeRepository;
    @Autowired
    private ItemRepository itemRepository;

    public CategoryItemSteps() {

        Given("category with id:1 has following attribute", (DataTable dataTable) -> {
            List<Map<String, String>> mapList = dataTable.asMaps(String.class, String.class);
            for (Map<String, String> map : mapList) {
                AttributeDto attributeDto = new ObjectMapper().convertValue(map, AttributeDto.class);
                Attribute attribute = new Attribute();
                attribute.setName(attributeDto.getName());
                attribute.setRequired(attributeDto.isRequired());
                Optional<Category> category = categoryRepository.findById(1L);
                Optional<AttributeType> attributeType = attributeTypeRepository
                        .findById(attributeDto.getAttributeTypeId());
                attributeType.ifPresent(attribute::setAttributeType);
                category.ifPresent(attribute::setCategory);
                attributeRepository.save(attribute);
            }
        });

        Given("user wants to create/update an Item with the following properties", (DataTable dataTable) -> {
            testContext().reset();
            List<Map<String, String>> mapList = dataTable.asMaps(String.class, String.class);
            Map<String, String> map = mapList.get(0);
            ItemDto itemDto = new ObjectMapper().convertValue(map, ItemDto.class);
            super.testContext()
                    .setPayload(itemDto);
        });

        And("following ItemAttributes", (DataTable dataTable) -> {
            List<ItemDto.ItemAttribute> list = new ArrayList<>();
            List<Map<String, String>> mapList = dataTable.asMaps(String.class, String.class);
            for (Map<String, String> map : mapList) {
                list.add(new ObjectMapper().convertValue(map, ItemDto.ItemAttribute.class));
            }
            ItemDto itemDto = (ItemDto) (super.testContext()
                    .getPayload());
            itemDto.setItemAttributes(list);
        });

        When("user saves the new item for category with id:{string}, {string}",
                (String categoryId, String testContext) -> {
            String createItemUrl = "/api/v1/categories/" + categoryId + "/items";
            executePost(createItemUrl);
        });

        When("user updates the item with id:1, {string}",
                (String testContext) -> {
            String updateItemUrl = "/api/v1/items/" + 1;
            executePut(updateItemUrl);
        });

        Given("there is an Item with id:1 in category id:1 with following properties", (DataTable dataTable) -> {
            testContext().reset();
            List<Map<String, String>> mapList = dataTable.asMaps(String.class, String.class);
            Map<String, String> map = mapList.get(0);
            ItemDto itemDto = new ObjectMapper().convertValue(map, ItemDto.class);
            Item item = new Item();
            item.setName(itemDto.getName());
            Optional<Category> category = categoryRepository.findById(1L);
            category.ifPresent(item::setCategory);
            itemRepository.save(item);
        });

        Given("item with id:1 has following attributes", (DataTable dataTable) -> {
            testContext().reset();
            List<ItemDto.ItemAttribute> list = new ArrayList<>();
            List<Map<String, String>> mapList = dataTable.asMaps(String.class, String.class);
            for (Map<String, String> map : mapList) {
                list.add(new ObjectMapper().convertValue(map, ItemDto.ItemAttribute.class));
            }
            Item item = itemRepository.findById(1L).orElseThrow(InvalidStateException::new);
            for (ItemDto.ItemAttribute itemAttributeDto : list) {
                Attribute attribute = attributeRepository
                        .findById(itemAttributeDto.getAttributeId()).orElseThrow(InvalidStateException::new);
                ItemAttribute itemAttribute = new ItemAttribute();
                itemAttribute.setValue(itemAttributeDto.getValue());
                itemAttribute.setAttribute(attribute);
                item.addItemAttribute(itemAttribute);
            }
            itemRepository.save(item);
        });


    }
}
