package com.labforward.demo.bdd.stepdef;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.labforward.demo.entity.AttributeType;
import com.labforward.demo.repository.AttributeTypeRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class AttributeTypeSteps extends AbstractSteps implements En {

    @Autowired
    AttributeTypeRepository attributeTypeRepository;

    public AttributeTypeSteps() {
        Given("following types exist in the system", (DataTable dataTable) -> {
            List<Map<String,String>> mapList = dataTable.asMaps(String.class, String.class);
            for(Map<String, String> map : mapList) {
                attributeTypeRepository.save(new ObjectMapper().convertValue(map, AttributeType.class));
            }
        });

        Given("user wants to create an attributeType with following properties", (DataTable dataTable) -> {
            testContext().reset();
            List<Map<String,String>> mapList = dataTable.asMaps(String.class, String.class);
            Map<String, String> map = mapList.get(0);
            AttributeType attributeType =  new ObjectMapper().convertValue(map, AttributeType.class);
            super.testContext()
                    .setPayload(attributeType);
        });

        When("user saves the attributeType {string}",
                (String testContext) -> {
                    String createAttributeTypeUrl = "/api/v1/attributeTypes";
                    executePost(createAttributeTypeUrl);
        });

    }
}
