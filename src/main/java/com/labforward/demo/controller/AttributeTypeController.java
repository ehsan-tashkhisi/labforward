package com.labforward.demo.controller;

import com.labforward.demo.entity.AttributeType;
import com.labforward.demo.exception.ResourceNotFoundException;
import com.labforward.demo.repository.AttributeTypeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/attributeTypes")
@Tag(name = "AttributeTypes", description = "Access to attributeTypes")
public class AttributeTypeController {

    private final AttributeTypeRepository attributeTypeRepository;

    @Autowired
    public AttributeTypeController(AttributeTypeRepository attributeTypeRepository) {
        this.attributeTypeRepository = attributeTypeRepository;
    }

    @GetMapping
    @Operation(summary = "Get all attributeTypes")
    public ResponseEntity<Page<AttributeType>> getAll(Pageable pageable) {
        return ResponseEntity.ok(attributeTypeRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get attributeType by id")
    public ResponseEntity<AttributeType> getById(@PathVariable Long id) {
        AttributeType attributeType = attributeTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("attribute not found"));
        return ResponseEntity.ok(attributeType);
    }

    @PostMapping
    @Operation(summary = "Create new attributeType")
    public ResponseEntity<AttributeType> create(@Valid @RequestBody AttributeType attributeType) {
        AttributeType savedAttribute = attributeTypeRepository.save(attributeType);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedAttribute.getId()).toUri();
        return ResponseEntity.created(location).body(savedAttribute);
    }
}
