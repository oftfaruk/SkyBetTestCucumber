Feature: Update Fixture
  Scenario: Update Fixture 1
    When User updates fixture id 1
    Then "homeTeam" attribute  changes
    And  Status code should be 204

