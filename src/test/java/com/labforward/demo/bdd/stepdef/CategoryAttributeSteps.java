package com.labforward.demo.bdd.stepdef;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.labforward.demo.dto.category.AttributeDto;
import com.labforward.demo.repository.AttributeTypeRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CategoryAttributeSteps extends AbstractSteps implements En {

    @Autowired
    private AttributeTypeRepository attributeTypeRepository;

    public CategoryAttributeSteps() {
        Given("user wants to create an attribute with the following properties",
                (DataTable attributeDt) -> {
            testContext().reset();
            List<Map<String,String>> mapList = attributeDt.asMaps(String.class, String.class);
            Map<String, String> map = mapList.get(0);
            AttributeDto attributeDto =  new ObjectMapper().convertValue(map, AttributeDto.class);

            // First row of DataTable has the attribute attributes hence calling get(0) method.
            super.testContext()
                    .setPayload(attributeDto);
        });

        When("user saves the new attribute for category with id:{string}, {string}",
                (String categoryId, String testContext) -> {
            String createAttributeUrl = "/api/v1/categories/"+ categoryId + "/attributes";
            executePost(createAttributeUrl);
        });

        When("user saves the attribute {string}", (String testContext) -> {
            String createAttributeUrl = "/api/v1/categories/{id}/attributes";
            executePut(createAttributeUrl);
        });

        Then("the (save/get/delete) {string}", (String expectedResult) -> {
            Response response = testContext().getResponse();

            switch (expectedResult) {
                case "IS SUCCESSFUL":
                    assertThat(response.statusCode()).isIn(200, 201);
                    break;
                case "FAILS":
                    assertThat(response.statusCode()).isBetween(400, 504);
                    break;
                default:
                    fail("Unexpected error");
            }
        });
    }

}
