package com.labforward.demo.weblayer.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labforward.demo.controller.cetegory.CategoryAttributeController;
import com.labforward.demo.dto.category.AttributeDto;
import com.labforward.demo.entity.Attribute;
import com.labforward.demo.entity.AttributeType;
import com.labforward.demo.entity.Category;
import com.labforward.demo.repository.AttributeRepository;
import com.labforward.demo.repository.AttributeTypeRepository;
import com.labforward.demo.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(CategoryAttributeController.class)
@ExtendWith(MockitoExtension.class)
class CategoryAttributeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private AttributeRepository attributeRepository;

    @MockBean
    private AttributeTypeRepository attributeTypeRepository;

    private JacksonTester<AttributeDto> jsonRequest;
    private JacksonTester<Attribute> jsonResponse;

    private Category category;
    private AttributeType attributeType;
    private Attribute attribute;
    private AttributeDto attributeDto;


    @BeforeEach
    void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        attributeDto = new AttributeDto();
        attributeDto.setAttributeTypeId(1L);
        attributeDto.setRequired(false);
        attributeDto.setName("TEST");

        category = new Category();
        category.setId(1L);
        attributeType = new AttributeType();
        attributeType.setId(attributeDto.getAttributeTypeId());
//{"id":1,"name":"TEST","required":false,"attributeType":{"id":1,"name":null,"valueType":null,"unitType":null}}
        attribute = new Attribute();
        attribute.setId(1L);
        attribute.setName(attributeDto.getName());
        attribute.setRequired(true);
        attribute.setAttributeType(attributeType);
        attribute.setCategory(category);
        attribute.setRequired(attributeDto.isRequired());

//        attributeType.setId(1L);

    }

    @Test
    void shouldReturnCreatedAttributeInBody() throws Exception {
        //Given
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));
        given(attributeTypeRepository.findById(attributeType.getId())).willReturn(Optional.of(attributeType));
        given(attributeRepository.save(any())).willReturn(attribute);

        //When
        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/categories/{id}/attributes", attribute.getCategory().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequest.write(attributeDto).getJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString())
                .isEqualTo(jsonResponse.write(attribute).getJson());

    }

}
