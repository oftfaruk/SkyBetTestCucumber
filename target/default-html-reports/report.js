$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("file:src/test/resources/features/fixture.feature");
formatter.feature({
  "name": "Verify Fixtures",
  "description": "",
  "keyword": "Feature"
});
formatter.scenario({
  "name": "",
  "description": "",
  "keyword": "Scenario",
  "tags": [
    {
      "name": "@hw"
    }
  ]
});
formatter.step({
  "name": "user retrieve fixtures",
  "keyword": "When "
});
formatter.match({
  "location": "Fixtures.user_retrieve_fixtures()"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "there are 3 fixtures within the returned object",
  "keyword": "Then "
});
formatter.match({
  "location": "Fixtures.there_are_fixtures_within_the_returned_object(Integer)"
});
formatter.result({
  "status": "passed"
});
});