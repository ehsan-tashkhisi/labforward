Feature: Create Category

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