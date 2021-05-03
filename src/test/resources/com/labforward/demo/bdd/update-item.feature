Feature: Update an Item

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
