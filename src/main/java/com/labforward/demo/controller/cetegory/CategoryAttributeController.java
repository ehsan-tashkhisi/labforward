package com.labforward.demo.controller.cetegory;

import com.labforward.demo.dto.category.AttributeDto;
import com.labforward.demo.entity.Attribute;
import com.labforward.demo.entity.AttributeType;
import com.labforward.demo.entity.Category;
import com.labforward.demo.exception.AttributeAlreadyExistException;
import com.labforward.demo.exception.ResourceNotFoundException;
import com.labforward.demo.repository.AttributeRepository;
import com.labforward.demo.repository.AttributeTypeRepository;
import com.labforward.demo.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Category Attributes", description = "Access to category attributes")
public final class CategoryAttributeController {


    private final AttributeRepository attributeRepository;
    private final CategoryRepository categoryRepository;
    private final AttributeTypeRepository attributeTypeRepository;

    @Autowired
    public CategoryAttributeController(AttributeRepository attributeRepository,
                                       CategoryRepository categoryRepository,
                                       AttributeTypeRepository attributeTypeRepository) {
        this.attributeRepository = attributeRepository;
        this.categoryRepository = categoryRepository;
        this.attributeTypeRepository = attributeTypeRepository;
    }

    @PostMapping("/{id}/attributes")
    @Operation(summary = "Add new Attribute to category")
    public ResponseEntity<Attribute> createAttribute(
            @PathVariable
            @Parameter(description = "Id of the category to which we want to add attribute") long id,
            @Valid @RequestBody AttributeDto attributeDto) {
        Category category = getCategory(id);
        AttributeType attributeType = attributeTypeRepository.findById(attributeDto.getAttributeTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("attribute type not found"));
        Attribute attribute = new Attribute();
        attribute.setName(attributeDto.getName());
        boolean isNamePresent = category
                .getAttributes()
                .stream().anyMatch(a -> a.getName().equals(attributeDto.getName()));
        if(isNamePresent)
            throw new AttributeAlreadyExistException();
        attribute.setCategory(category);

        attribute.setAttributeType(attributeType);
        attribute.setRequired(attributeDto.isRequired());
        Attribute savedAttribute = attributeRepository.save(attribute);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}")
                .buildAndExpand(attribute.getId()).toUri();
        return ResponseEntity.created(location).body(savedAttribute);
    }

    @GetMapping("/{id}/attributes")
    @Operation(summary = "Get all attributes of a category")
    public ResponseEntity<Collection<Attribute>> getAttributes(@PathVariable long id) {
        Category category = getCategory(id);
        return ResponseEntity.ok(category.getAttributes());
    }

    private Category getCategory(@PathVariable long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("category not found"));
    }

}
