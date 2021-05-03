package com.labforward.demo.weblayer.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labforward.demo.controller.cetegory.CategoryController;
import com.labforward.demo.entity.Category;
import com.labforward.demo.repository.CategoryRepository;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(CategoryController.class)
@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryRepository categoryRepository;

    private JacksonTester<Category> jsonRequest;
    private JacksonTester<Category> jsonResponse;


    @BeforeEach
    void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    void shouldCreateCategoryWhenPost() throws Exception {
        //Given
        Category category = new Category();
        category.setName("category1");
        given(categoryRepository.save(any())).willReturn(category);

        //When
        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequest.write(category).getJson()))
                .andReturn().getResponse();

        //Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString())
                .isEqualTo(jsonResponse.write(category).getJson());
    }

    @Test
    void shouldReturnWhenGet() throws Exception {
        //Given
        Category mockCategory = new Category();
        mockCategory.setName("category1");
        given(categoryRepository.findById(1L)).willReturn(Optional.of(mockCategory));

        //When
        MockHttpServletResponse response = mockMvc.perform(
                get("/api/v1/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequest.write(mockCategory).getJson()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        //Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(jsonResponse.write(mockCategory).getJson());
    }

    @Test
    public void shouldReturnAllCategories() throws Exception {
        Category category = new Category();
        category.setName("category1");
        Category anotherCategory = new Category();
        anotherCategory.setName("category2");
        Page<Category> page = new PageImpl<>(Arrays.asList(category, anotherCategory));
        given(categoryRepository.findAll(any(Pageable.class))).willReturn(page);
        mockMvc.perform(get("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", Is.is(category.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name", Is.is(anotherCategory.getName())));
    }
}
