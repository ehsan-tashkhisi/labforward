Feature: Add new Attribute to Category

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