Feature: Delete Fixture

  Scenario: Delete Fixture id=3

    When User deletes a fixture with id 3
    Then Status code should be 204
    And  User retrieves fixture with id 3
    Then Status code should be 404



