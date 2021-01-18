Feature: Post Fixture

  Scenario:Create new Fixture
    When User post a new fixture
    Then Status code should be 202
    And  The object at 0 index has a "teamId" of "HOME"




