package com.labforward.demo.unit;

import com.labforward.demo.entity.Attribute;

public class TestHelper {

    public static Attribute getAttribute(long id, String name, boolean required) {
        Attribute attribute = new Attribute();
        attribute.setId(id);
        attribute.setRequired(required);
        attribute.setName("name");
        return attribute;
    }
}
