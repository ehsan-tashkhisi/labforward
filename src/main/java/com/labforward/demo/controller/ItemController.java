package com.labforward.demo.controller;

import com.labforward.demo.dto.category.ItemDto;
import com.labforward.demo.entity.Item;
import com.labforward.demo.exception.ResourceNotFoundException;
import com.labforward.demo.repository.ItemRepository;
import com.labforward.demo.service.ItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/items")
@Tag(name = "Items", description = "Access to all Items")
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

    @Autowired
    public ItemController(ItemService itemService,
                          ItemRepository itemRepository) {
        this.itemService = itemService;
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public ResponseEntity<Page<Item>> getAll(Pageable pageable) {
        return ResponseEntity.ok(itemRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getById(@PathVariable Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("item not found"));
        return ResponseEntity.ok(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable long id, @RequestBody ItemDto itemDto) {
        Item item = itemService.updateItem(id, itemDto);
        return ResponseEntity.ok(item);
    }
}
