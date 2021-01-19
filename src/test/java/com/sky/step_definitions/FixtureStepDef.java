package com.sky.step_definitions;


import com.github.javafaker.Faker;
import com.sky.utilities.ConfigurationReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class FixtureStepDef {


    Response response;
    String url = ConfigurationReader.get("api_uri");


    @When("User retrieves fixtures")
    public void userRetrievesFixtures() {

        response = given().accept(ContentType.JSON).when().get(url + "/fixtures");
    }


    @Then("There should be {int} fixtures within the returned object")
    public void thereShouldBeFixturesWithinTheReturnedObject(Integer num) {
        int expectedFixNum = num;
        assertEquals(response.statusCode(), 200);
        assertTrue(response.contentType().contains("application/json"));

        JsonPath jsonPath = response.jsonPath();
        List<Object> fixtureId = jsonPath.getList("fixtureId");
        List<Object> fixtureStatus = jsonPath.getList("fixtureStatus");

        //   Assert that there are 3 fixtures within the returned object.
        assertEquals(fixtureId.size(), 3);
        assertEquals(fixtureStatus.size(), 3);


    }


    @Then("{string} value must be bigger than {int}")
    public void valueMustBeBiggerThan(String fixtureId, int value) {
        JsonPath jsonPath = response.jsonPath();
        List<Object> fixtureIds = jsonPath.getList(fixtureId);

        for (int i = 0; i < fixtureIds.size(); i++) {

            assertTrue(jsonPath.getInt("fixtureId[" + i + "]") > value);

        }


    }

    @When("User deletes a fixture with id {int}")
    public void userDeletesAFixtureWithId(int id) {
        String uri = url + "/fixture/{id}";
        given().pathParam("id", id).when().delete(uri).then().statusCode(204);

    }


    @And("User retrieves fixture with id {int}")
    public void userRetrievesFixtureWithId(int id) {
        String uri = url + "/fixture/{id}";
        response = given().accept(ContentType.JSON).pathParam("id", id).when().get(uri);
    }



    private int getFixturesSize() {
        Response response = given().accept(ContentType.JSON)
                .when().get(url + "/fixtures");
        List<Map<String, Object>> allFixtures = response.body().as(List.class);

        return allFixtures.size();
    }

    int postid;

    @When("User post a new fixture")
    public void userPostANewFixture() throws IOException, InterruptedException {

        postid = getFixturesSize() + 1;
        String fileString = new String(Files.readAllBytes(Paths.get("createfixture.json")));

        fileString.replace("new", String.valueOf(postid));

        String newFixture = new String(Files.readAllBytes(Paths.get("createfixture.json")));
        ValidatableResponse response = given().contentType(ContentType.JSON)
                .and().body(newFixture)
                .when().post("/fixture").then().assertThat().statusCode(202);

      Thread.sleep(60000);

    }


    @And("The object at {int} index has a {string} of {string}")
    public void theObjectAtIndexHasAOf(int index, String attribute, String value) {

        Response newFixtureResponse = given().accept(ContentType.JSON)
                .and().pathParam("id", postid)
                .when().get("/fixture/{id}");


        JsonPath json = newFixtureResponse.jsonPath();

        //assert within the teams array, that the first object has a teamId of 'HOME'.
        String teamId = json.getString("footballFullState.teams[" + index + "]." + attribute + "");
        assertEquals(teamId, value);


    }

    String teamname;

    @When("User updates fixture id {int}")
    public void userUpdatesFixtureId(int updateId) throws IOException {
        Faker faker = new Faker();
        teamname = faker.team().name();
        String newFixture = changeAttribute(teamname);

        response = given().contentType(ContentType.JSON).and()
                .and().body(newFixture)
                .when().put(url + "/fixture");
    }

    private String changeAttribute(String team) throws IOException {

        String fileString = new String(Files.readAllBytes(Paths.get("changeFixture.json")));

        return fileString.replace("team_must_be_assigned", String.valueOf(team));

    }

    @And("{string} attribute  changes")
    public void attributeChanges(String attribute) {

        Response updatedFixture = given().accept(ContentType.JSON)
                .and().pathParam("id", "1")
                .when().get(url + "/fixture/{id}");

        assertEquals(updatedFixture.statusCode(), 200);

        JsonPath json = updatedFixture.jsonPath();

        String hometeam = json.getString("footballFullState." + attribute + "");

        // Assert that the relevant attributes in the fixture have changed.
        assertEquals(hometeam, teamname);

    }

    @Then("Status code should be {int}")
    public void statusCodeShouldBe(int statusCode) {
        assertEquals(statusCode, response.statusCode());
    }


}
