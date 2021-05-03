Feature: Create new Item

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
