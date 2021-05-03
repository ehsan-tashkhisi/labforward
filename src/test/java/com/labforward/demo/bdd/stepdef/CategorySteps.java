package com.labforward.demo.bdd.stepdef;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.labforward.demo.entity.Category;
import com.labforward.demo.repository.CategoryRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class CategorySteps extends AbstractSteps implements En {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategorySteps() {
        Given("user wants to create a category with following property", (DataTable dataTable) -> {
            testContext().reset();
            List<Map<String,String>> mapList = dataTable.asMaps(String.class, String.class);
            Map<String, String> map = mapList.get(0);
            Category category =  new ObjectMapper().convertValue(map, Category.class);
            super.testContext()
                    .setPayload(category);
        });

        When("user saves the category {string}",
                (String testContext) -> {
                    String createCategoryUrl = "/api/v1/categories";
                    executePost(createCategoryUrl);
        });

        Given("there is a category with id:1 and following property", (DataTable dataTable) -> {
            Category category = new Category();
            category.setName(dataTable.asMaps().get(0).get("name"));
            categoryRepository.save(category);
        });
    }
}
