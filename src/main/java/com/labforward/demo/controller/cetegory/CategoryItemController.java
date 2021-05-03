package com.labforward.demo.controller.cetegory;

import com.labforward.demo.dto.category.ItemDto;
import com.labforward.demo.entity.Category;
import com.labforward.demo.entity.Item;
import com.labforward.demo.exception.ResourceNotFoundException;
import com.labforward.demo.repository.CategoryRepository;
import com.labforward.demo.repository.ItemRepository;
import com.labforward.demo.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Category Items", description = "Access to category items")
public final class CategoryItemController {

    private final ItemService itemService;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public CategoryItemController(ItemService itemService,
                                  ItemRepository itemRepository,
                                  CategoryRepository categoryRepository) {
        this.itemService = itemService;
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
    }

    @PostMapping("/{id}/items")
    @Operation(summary = "Add new Item in category",
            description = "Here you can add Item to the category but attributes you define in the body should " +
                    "belongs to this category and values for those attributes should have appropriate types regarding " +
                    "attributes of the category, otherwise status code 400 will returns")
    public ResponseEntity<Item> createItem(@PathVariable long id, @RequestBody ItemDto itemDto) {
        Category category = getCategory(id);
        Item item = itemService.createItem(category, itemDto);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/attributes/{id}")
                .buildAndExpand(id).toUri();
        return ResponseEntity.created(location).body(item);
    }

    @GetMapping("/{id}/items")
    @Operation(summary = "Get All item in the category")
    public ResponseEntity<Page<Item>> getAllItems(@PathVariable long id, Pageable pageable) {
        Category category = getCategory(id);
        Page<Item> items = itemRepository.findByCategory(category, pageable);
        return ResponseEntity.ok(items);
    }

    private Category getCategory(@PathVariable long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

}
