# How it works

This is the sample project for managing different kinds of items with different categories in Labforward. Each item
belongs to a category and can have attributes in that category. We should first define Categories which have some
attributes then we create and add item to those categories. Item's Category can not be changed during its lifetime so
each item can belongs only to one category during its lifetime. Each Category has a specific name and id and a set of
attributes of different types. attribute type is also another entity which has a valueType (eg:INTEGER, STRING,
DOUBLE,...) and a unitType (eg:METER, DOLOR, ...). So first we define our category and then add attribute to a defined
category Then we create Item in that Category. Following is data model for our app. Some constrains are not in place 
for future changes, for instance in the future maybe we need to maintain previous value for each itemAttributes, 
so it has its own primary key. On the other hand AttributeType is better not to be exposed to the end users,
and I did not handle their response code in the best way. The instances of this entity is better to be created by administrator
and Developers.
In this data model all values of valueType are stored in varchar but validation is in place in our application, 
so all valueType will be saved in their valid format but indexing and searching this value will be difficult in the future we can
define separate tables for each ValueType, but we should consider all constraints in that case.

![alt text](https://www.linkpicture.com/q/data-model.png)

For th sake of simplicity I have used h2 in memory database but changing our database is just a matter of changing configuration in
application.properties file.

I did not use separate branch for each feature, and I worked on master branch. In real situation I always
create separate branch for each feature.

## Validation

Apart from other validations we have strict validation for creating Item in a category. Item should have all
required attribute in that category And types of values for each of ItemAttribute for Item should adhere to 
type of corresponding attribute, no item can be saved if any constraint violated. AttributeValidationManager is written
in a way that make attribute validation based on their metadata very easy ann it can be extended.

ValueTypes in our system is just an Enum in Java (although we can change it in the future), defining new valueType is
just a matter of adding new type to that enum. Each time we define new ValueType we should define its validator 
by implementing ValueTypeValidator Interface. If we want to save value for valueType that has no associated valueType
NoValueTypeValidator exception will be thrown. In the future we can also check existence of Validator for each valueType
during startup or simple test case, but this should be considered on crating new item too.

## How to run

You just need to clone the repository and run following command:

```bash
mvn clean install
mvn spring-boot:run
```

## Usage And API Documentation

For using this application after submitting previous commands you can access OPEN API 3
documentation for its api under http://localhost:8080/v3/api-docs/

Also, for testing and working with the api you can use swagger-ui which is available 
in http://localhost:8080/swagger-ui.html.

![alt text](https://www.linkpicture.com/q/swagger.png)

# Future works
Cucumber is great for BDD, but it does not work well for API testing if I had enough time I would use 
some other tools like Postman to test my API in a better way, Also I have not tested body and all headers
of the response in my Cucumber features. It would be great to test them too. 

I am also used to create separate branch for each feature but here for simplicity I worked on master branch.

Better Response status code handling.

Sometimes I try to consider other approaches like document based database or NoSql depending on the requirements.

To my mind we can Also have more Unit Tests to make sure each individual unit works properly.

Considering MapStruct for Converting DTOs to entity would be great. 

We should Think about locking mechanist to ensure no data loss in some parts.

How we want to generate report from our data.

We should have better approach for running test in maven. Right now I am just running them in intellij IDEA,
but we should have plan to group tests to be run in different stages in our CI/CD.

Having Separate table for valueTypes, is that good?

# Cucumber Test Features
Developing using BDD methodology I have defined some scenario like below that are passed.
I believe that nothing is more descriptive than putting them here:

# Feature: Create AttributeType

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

# Feature: Create Category

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

# Feature: Add new Attribute to Category

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

# Feature: Create new Item

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

# Feature: Update an Item

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

![alt text](https://www.linkpicture.com/q/cucumber-scenario.png)

![alt text](https://www.linkpicture.com/q/scnario-chart.png)

![alt text](https://www.linkpicture.com/q/cucumber-scenario-test.png)