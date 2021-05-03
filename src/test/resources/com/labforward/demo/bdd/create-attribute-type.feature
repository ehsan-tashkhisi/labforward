Feature: Create AttributeType

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