package com.labforward.demo.unit.service;

import com.labforward.demo.dto.category.ItemDto;
import com.labforward.demo.entity.Category;
import com.labforward.demo.entity.Item;
import com.labforward.demo.entity.ItemAttribute;
import com.labforward.demo.exception.ItemAttributeValidationException;
import com.labforward.demo.repository.ItemRepository;
import com.labforward.demo.service.ItemService;
import com.labforward.demo.unit.TestHelper;
import com.labforward.demo.validator.ItemValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    private ItemService itemService;

    @Mock
    private ItemValidator itemValidator;

    @Mock
    private ItemRepository itemRepository;
    private Category category;
    private ItemDto itemDto;

    @BeforeEach
    void setup() {
        this.itemService = new ItemService(itemRepository, itemValidator);
        category = new Category();
        category.setId(1L);
        category.getAttributes().add(TestHelper.getAttribute(1, "", true));
        category.getAttributes().add(TestHelper.getAttribute(2, "", false));
        itemDto = new ItemDto();
        itemDto.getItemAttributes().add(new ItemDto.ItemAttribute(1, ""));
        itemDto.getItemAttributes().add(new ItemDto.ItemAttribute(2, ""));
        itemDto.getItemAttributes().add(new ItemDto.ItemAttribute(3, ""));
    }

    @Test
    void shouldSaveItemWithAssociatedCategoryWhenDtoIsValid() {
        doNothing().when(itemValidator).validate(any(), any());
        itemService.createItem(category, itemDto);
        verify(itemRepository)
                .save(argThat(a -> (a.getCategory().getId().equals(category.getId()))));
    }

    @Test
    void shouldHaveAllAttributesInDtoWhenUpdate() {
        doNothing().when(itemValidator).validate(any(), any());
        Item item = new Item();
        item.setCategory(category);
        when(itemRepository.findItemByIdForUpdate(1L)).thenReturn(Optional.of(item));
        itemService.updateItem(1, itemDto);
        verify(itemRepository)
                .save(argThat(a ->
                        (a.getItemAttributes().size() == (itemDto.getItemAttributes().size()))));
    }

    @Test
    void shouldAllPreviousAttributeReplacedByNewOnesWhenUpdate() {
        doNothing().when(itemValidator).validate(any(), any());
        Item item = new Item();
        item.setCategory(category);
        ItemAttribute itemAttribute = new ItemAttribute();
        itemAttribute.setAttribute(TestHelper.getAttribute(1, "", true));
        item.addItemAttribute(itemAttribute);
        itemDto = new ItemDto();
        itemDto.getItemAttributes().add(new ItemDto.ItemAttribute(2, ""));
        when(itemRepository.findItemByIdForUpdate(1L)).thenReturn(Optional.of(item));
        itemService.updateItem(1, itemDto);
        verify(itemRepository).save(argThat(a ->
                a.getItemAttributes().stream().noneMatch(i -> i.getAttribute().getId() == 1)));
        verify(itemRepository).save(argThat(a ->
                a.getItemAttributes().stream().anyMatch(i -> i.getAttribute().getId() == 2)));
    }

    @Test
    void shouldSaveExactNumberOfDto() {
        doNothing().when(itemValidator).validate(any(), any());
        itemService.createItem(category, itemDto);
        verify(itemRepository).save(argThat(item ->
                (item.getItemAttributes().size() == itemDto.getItemAttributes().size())));
    }

    @Test
    void shouldThrowExceptionWhenItemPostDtoIsInvalid() {
        Mockito.doThrow(ItemAttributeValidationException.class).when(itemValidator).validate(any(), any());
        assertThrows(ItemAttributeValidationException.class, () -> itemService.createItem(category, itemDto));
    }

}
