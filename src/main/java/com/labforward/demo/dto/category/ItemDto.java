package com.labforward.demo.dto.category;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemDto {

    @NotBlank
    private String name;
    private List<ItemAttribute> itemAttributes = new ArrayList<>();

    @Data
    public static class ItemAttribute {

        public ItemAttribute() {

        }

        public ItemAttribute(long attributeId, String value) {
            this.attributeId = attributeId;
            this.value = value;
        }

        @NotNull
        private long attributeId;
        @NotBlank
        private String value;
    }


}
