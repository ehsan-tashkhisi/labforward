package com.labforward.demo.dto.category;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AttributeDto {

    @NotBlank
    private String name;
    @NotNull
    private  Long attributeTypeId;
    @NotNull
    private boolean required;

}
