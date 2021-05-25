# How it works

This is the sample project for managing different kinds of items with different categories in Labforward. Each item
belongs to a category and can only have attributes in that category. In order to create an item from scratch we should
call APIs in specific sequence. After running the application and opening swagger-ui in your browser (as described in
how to run section in the following) you can see APIs are grouped and shown in appropriate order.

### Here is the complete flow to create an Item from scratch.

* First we should define AttributeTypes which has a valueType (eg:INTEGER, STRING, DOUBLE,...) and a unitType (eg:
  METER, DOLOR, ...) (using a POST request to the API grouped as AttributeTypes).

* Then we define Category with specific name (using a POST request to the API grouped as Categories).

* Then after we add attributes (using attributeTypeId of the AttributeTypes we defined earlier) to that category (using a
  POST request to the API grouped as Category Attributes)

* Then after we add an Item into that category (using a POST request to the API grouped as Category Items).

* Updating an item can be done using a PUT request to the API grouped as Items.

Item's category can not be changed during its lifetime so each item can belongs only to one category during its
lifetime. This lifecycle dependency is shown in the url of our API, for instance for creating item in category with id:1
we will make a POST request to the following URL /api/categories/1/items/. Some constrains are not in place for future
changes, for instance in the future maybe we need to maintain previous value for each itemAttributes, so it has its own
primary key. On the other hand AttributeType is better not to be exposed to the end users, and I did not handle their
response code in the best way. The instances of this entity is better to be created by administrator and developers. In
this data model all values of valueType are stored in varchar but validation is in place in our application, so all
ValueType will be saved in their valid format but indexing and searching this value will be difficult in the future we
can define separate tables for each ValueType, however, we should consider all constraints in that case.

Special care should be taken on when we want to add attribute to a category. We should decide whether we want to let the
user add attribute to the Category even after an Item is created for that category, or we can only add attribute to the
category only if it has no item. Right now we let the user add attribute to the category even when there is an item in
that category. This lets some item in our category to not have some of required item, however, it gives us some
flexibility. We can also have default value for those attribute in the future or even don't let the user add attribute
to the category that already has item in it.

For updating item right now we don't have any HTTP PATCH like operation, in other words we don't support updating the
item partially. We have provided PUT operation, so the clients of our API should provide all the attributes for the item
being updated in the body of the request, and if she does not provide all required attributes for item in associated
category, 400 bad request with corresponding errors in the body will returns. In the future we can support HTTP PATCH
operation for partial update.

![image 4](https://github.com/ehsan-tashkhisi/labforward-report/blob/master/images/data-model.png?raw=true)

For the sake of simplicity I have used h2 in memory database(Also I have tested it on PostgreSQL) but changing our
database is just a matter of changing configuration in application.properties file.

It will be better to provide schema for the database instead of letting JPA create it automatically.

I did not use separate branch for each feature, and I worked on master branch. In real situation I always
create separate branch for each feature.

## Validation

Apart from other validations we have strict validation for creating Item in a Category. Item should have all required
attributes in that category and types of values for each of ItemAttribute for Item should adhere to type of
corresponding attribute in the category, no item can be saved if any constraint violated. AttributeValidationManager is
implemented in a way that make attribute validation based on their metadata very easy, and it can be extended.
AttributeValidationManager use injected AttributeValidators to validate attributes based on different criteria, so
developer can add her own AttributeValidator to validate attribute on arbitrary criteria. Right now there is only one
ItemAttributeValidator implementation which validate attributes based on their type(ItemAttributeValueTypeValidator).

In order to be more flexible, ItemAttributeValueTypeValidator itself does not validate valueTypes of attributes and only
delegates to valueTypeValidators for validation. So developer can add her own ValueTypeValidator to validate new
ValueTypes defined in the system.

ValueTypes in our system is just an enum in Java (although we can change it in the future), defining new ValueType is
just a matter of adding new type to that enum. Each time we define new ValueType we should define its validator 
by implementing ValueTypeValidator Interface. If we want to save value for valueType that has no associated ValueType
NoValueTypeValidatorException will be thrown. In the future we can also check existence of Validator for each ValueType
during startup or simple test case, but this should be considered on crating new item too.

## How to run

You just need to clone the repository and run following command:

```bash
mvn clean install
mvn spring-boot:run
```

## Usage And API Documentation

For using this application after submitting previous commands you can access OpenApi version 3
specification for its API under http://localhost:8080/v3/api-docs/

Also, for testing and working with the api you can use swagger-ui which is available 
in http://localhost:8080/swagger-ui.html.

![swagger](https://github.com/ehsan-tashkhisi/labforward-report/blob/master/images/swagger.png?raw=true)

# Future works
Cucumber is great for BDD, but it does not work well for API testing if I had enough time I would use 
some other tools like Postman to test my API in a better way, Also I have not tested body and all headers
of the response in my Cucumber features. It would be great to test them too. 

I am also used to create separate branch for each feature but here for simplicity I worked on master branch.

Better response status code handling

Sometimes I try to consider other approaches like document based database or NoSql depending on the requirements.

To my mind we can also have more Unit Tests to make sure each individual unit works properly.

Considering MapStruct for converting DTOs to entity would be great. 

We should think about locking mechanist to ensure no data loss in some parts.

How we want to generate report from our data.

We should have better approach for running test in maven. Right now I am just running them in IntelliJ IDEA,
but we should have plan to group tests to be run in different stages in our CI/CD.

Having separate table for valueTypes, is that good?

# Cucumber Test Features
Developing using BDD methodology I have defined some scenarios like below that are passed.
I believe that nothing is more descriptive than putting them here:

### Feature: Create AttributeType

    Scenario: WITH ALL REQUIRED FIELDS IS SUCCESSFUL

    Given user wants to create an attributeType with following properties
      |  name            | valueType | unitType |
      |  INTEGER-METER   | INTEGER   | METER    |

    When user saves the attributeType 'WITH ALL REQUIRED FIELDS'
    Then the save 'IS SUCCESSFUL'


    Scenario Outline: <testCase> <expectedResult>

    Given user wants to create an attributeType with following properties
      |  name  |  valueType  |  unitType  |
      | <name> | <valueType> | <unitType> |

    When user saves the category '<testCase>'
    Then the save '<expectedResult>'

    Examples:
      | testCase                         | expectedResult | name          | valueType | unitType |
      | WITHOUT       NAME               | FAILS          |               | INTEGER   | METER    |
      | WITH ALL REQUIRED FIELDS         | IS SUCCESSFUL  | Category1     | DOUBLE    | DOLOR    |

### Feature: Create Category

    Scenario: WITH ALL REQUIRED FIELDS IS SUCCESSFUL

    Given user wants to create a category with following property
      |  name      |
      | Category1  |

    When user saves the category 'WITH ALL REQUIRED FIELDS'
    Then the save 'IS SUCCESSFUL'


    Scenario Outline: <testCase> <expectedResult>

    Given user wants to create an attribute with the following properties
      |  name  |
      | <name> |

    When user saves the category '<testCase>'
    Then the save '<expectedResult>'

    Examples:
      | testCase                         | expectedResult | name          |
      | WITHOUT       NAME               | FAILS          |               |
      | WITH ALL REQUIRED FIELDS         | IS SUCCESSFUL  | Category1     |

### Feature: Add new Attribute to Category

    Scenario: WITH ALL REQUIRED FIELDS IS SUCCESSFUL
    
    Given following types exist in the system
    | name  | valueType | unitType |
    | type1 | INTEGER   | METER    |
    | type2 | STRING    | DOLOR    |
    
    Given there is a category with id:1 and following property
    | name      |
    | category1 |
    
    Given user wants to create an attribute with the following properties
    |  name      | attributeTypeId | required |
    | Attribute1 | 1               | true     |

    When user saves the new attribute for category with id:'1', 'REQUIRED FIELDS IS SUCCESSFUL'
    Then the save 'IS SUCCESSFUL'


    Scenario Outline: <testCase> <expectedResult>
    Given following types exist in the system
    | name  | valueType | unitType |
    | type1 | INTEGER   | METER    |
    | type2 | STRING    | DOLOR    |
    
    Given there is a category with id:1 and following property
    | name      |
    | category1 |
    
    Given user wants to create an attribute with the following properties
    |  name  |  attributeTypeId  |  required  |
    | <name> | <attributeTypeId> | <required> |

    When user saves the new attribute for category with id:'1', '<testCase>'
    Then the save '<expectedResult>'

    Examples:
      | testCase                         | expectedResult | name          | attributeTypeId | required |
      | WITHOUT       NAME               | FAILS          |               | 1               | true     |
      | WITHOUT       ATTRIBUTE TYPE ID  | FAILS          | Attribute1    |                 | true     |
      | WITH ALL REQUIRED FIELDS         | IS SUCCESSFUL  | Attribute2    | 2               | true     |

    Scenario Outline: <testCase> <expectedResult>

    Given user wants to create an attribute with the following properties
      |  name  |  attributeTypeId  |  required  |
      | <name> | <attributeTypeId> | <required> |

    When user saves the new attribute for category with id:'2', '<testCase>'
    Then the save '<expectedResult>'

    Examples:
      | testCase                         | expectedResult | name          | attributeTypeId | required |
      | WITHOUT       NAME               | FAILS          |               | 1               | true     |
      | WITHOUT       ATTRIBUTE TYPE ID  | FAILS          | Attribute1    |                 | true     |
      | WITH ALL REQUIRED FIELDS         | FAILS          | Attribute2    | 2               | true     |

### Feature: Create new Item

    Scenario: WITH ALL REQUIRED FIELDS IS SUCCESSFUL
    Given following types exist in the system
    | name  | valueType | unitType |
    | type1 | INTEGER   | METER    |
    | type2 | STRING    | STRING   |

    Given there is a category with id:1 and following property
      | name      |
      | category1 |

    Given category with id:1 has following attribute
      | name       | attributeTypeId | required |
      | attribute1 | 1               | true     |
      | attribute2 | 2               | false    |


    Given user wants to create an Item with the following properties
      |  name      |
      |  item1     |

    And following ItemAttributes
      | attributeId | value |
      | 1           | 1     |
      | 2           | 2     |

    When user saves the new item for category with id:'1', 'REQUIRED FIELDS IS SUCCESSFUL'
    Then the save 'IS SUCCESSFUL'


    Scenario Outline: <testCase> <expectedResult>

    Given following types exist in the system
      | name  | valueType | unitType |
      | type1 | INTEGER   | METER    |
      | type2 | STRING    | STRING   |

    Given there is a category with id:1 and following property
      | name      |
      | category1 |

    Given category with id:1 has following attribute
      | name       | attributeTypeId | required |
      | attribute1 | 1               | true     |
      | attribute2 | 2               | false    |

    Given user wants to create an Item with the following properties
      | name |
      | 1    |

    And following ItemAttributes
      |  attributeId   |  value   |
      | <attributeId1> | <value1> |
      | <attributeId2> | <value2> |

    When user saves the new item for category with id:'1', '<testCase>'
    Then the save '<expectedResult>'

    Examples:
      | testCase                             | expectedResult | attributeId1 | value1   | attributeId2 | value2 |
      | WITH          TYPE MISMATCH          | FAILS          | 1            | true     | 2            | title  |
      | WITHOUT       REQUIRED ATTRIBUTE     | FAILS          | 1            |          | 2            | title  |
      | WITH          INVALID ATTRIBUTE ID   | FAILS          | 5            | 100      | 2            | title  |
      | WITH          REQUIRED ATTRIBUTE     | IS SUCCESSFUL  | 1            | 100      | 2            |        |
      | WITH          ALL ATTRIBUTE          | IS SUCCESSFUL  | 1            | 123456   | 2            | title  |

### Feature: Update an Item

    Scenario: WITH ALL REQUIRED FIELDS IS SUCCESSFUL
    Given following types exist in the system
    | name  | valueType | unitType |
    | type1 | INTEGER   | METER    |
    | type2 | STRING    | STRING   |

    Given there is a category with id:1 and following property
      | name      |
      | category1 |

    Given category with id:1 has following attribute
      | name       | attributeTypeId | required |
      | attribute1 | 1               | true     |
      | attribute2 | 2               | false    |


    Given there is an Item with id:1 in category id:1 with following properties
      |  name      |
      |  item1     |

    And item with id:1 has following attributes
      | attributeId | value |
      | 1           | 100   |
      | 2           | test  |

    When user wants to update an Item with the following properties
      |  name      |
      |  item1     |

    And following ItemAttributes
      | attributeId | value |
      | 1           | 1     |
      | 2           | 2     |

    When user updates the item with id:1, 'REQUIRED FIELDS IS SUCCESSFUL'
    Then the save 'IS SUCCESSFUL'

    Scenario Outline: <testCase> <expectedResult>

    Given following types exist in the system
      | name  | valueType | unitType |
      | type1 | INTEGER   | METER    |
      | type2 | STRING    | STRING   |

    Given there is a category with id:1 and following property
      | name      |
      | category1 |

    Given category with id:1 has following attribute
      | name       | attributeTypeId | required |
      | attribute1 | 1               | true     |
      | attribute2 | 2               | false    |

    Given there is an Item with id:1 in category id:1 with following properties
      |  name      |
      |  item1     |

    And item with id:1 has following attributes
      | attributeId | value |
      | 1           | 100   |
      | 2           | test  |

    Given user wants to update an Item with the following properties
      | name  |
      | item2 |

    And following ItemAttributes
      |  attributeId   |  value   |
      | <attributeId1> | <value1> |
      | <attributeId2> | <value2> |

    When user updates the item with id:1, '<testCase>'
    Then the save '<expectedResult>'

    Examples:
      | testCase                             | expectedResult | attributeId1 | value1   | attributeId2 | value2 |
      | WITH          TYPE MISMATCH          | FAILS          | 1            | string   | 2            | title  |
      | WITHOUT       DUPLICATE ATTRIBUTE ID | FAILS          | 1            | 100      | 1            | 200    |
      | WITHOUT       REQUIRED ATTRIBUTE     | FAILS          | 1            |          | 2            | title  |
      | WITH          INVALID ATTRIBUTE ID   | FAILS          | 1            | 100      | 1            | 500    |
      | WITH          REQUIRED ATTRIBUTE     | IS SUCCESSFUL  | 1            | 100      | 2            |        |
      | WITH          ALL ATTRIBUTE          | IS SUCCESSFUL  | 1            | 400      | 2            | title  |

# Reports for cucumber Scenario Tests

You can see Complete report in the following link:

https://ehsan-tashkhisi.github.io/

![cucumber-scenario](https://github.com/ehsan-tashkhisi/labforward-report/blob/master/images/cucumber-scenario.png?raw=true)

![scenario-chart](https://github.com/ehsan-tashkhisi/labforward-report/blob/master/images/scenario-chart.png?raw=true)

![cucumber-scenario-test](https://github.com/ehsan-tashkhisi/labforward-report/blob/master/images/cucumber-scenario-test.png?raw=true)
