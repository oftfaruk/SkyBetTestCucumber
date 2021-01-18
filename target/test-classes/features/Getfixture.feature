Feature: Verify Fixtures

  Background:

    When User retrieves fixtures
    Then Status code should be 200

  @hw
  Scenario: Fixtures Num
    Then There should be 3 fixtures within the returned object

  Scenario:Fixtures Id value
    Then "fixtureId" value must be bigger than 0
