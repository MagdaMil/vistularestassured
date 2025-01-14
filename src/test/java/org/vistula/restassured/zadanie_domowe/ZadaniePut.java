package org.vistula.restassured.zadanie_domowe;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.vistula.restassured.RestAssuredTest;
import java.util.concurrent.ThreadLocalRandom;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class ZadaniePut extends RestAssuredTest {

    @Test
    public void shouldCreateNewPlayer() {

        JSONObject requestParams = new JSONObject();

        int randomSalary = ThreadLocalRandom.current().nextInt(200, 50000);
        String myName = RandomStringUtils.randomAlphabetic(7);
        String myNationality = "English";

        requestParams.put("name", myName);
        requestParams.put("nationality", myNationality);
        requestParams.put("salary", randomSalary);

        Object idNewPlayer = given().header("Content-Type", "application/json")
                .body(requestParams.toString())
                .post("/information")
                .then()
                .log().all()
                .statusCode(201)
                .body("nationality", equalTo(myNationality))
                .body("name", equalTo(myName))
                .body("salary", equalTo(randomSalary))
                .body("id", greaterThan(0))// sprawdza, czy wartość jest dodatnia
                .extract().path("id");

        int newRandomSalary = ThreadLocalRandom.current().nextInt(200, 50000);
        String newName = RandomStringUtils.randomAlphabetic(9);
        String newNationality = "Polish";

        requestParams.put("name", newName);
        requestParams.put("nationality", newNationality);
        requestParams.put("salary", newRandomSalary);


        given().header("Content-Type", "application/json")
                .body(requestParams.toString())
                .put("/information/" + idNewPlayer)
                .then()
                .log().all()
                .statusCode(200)
                .body("nationality", equalTo(newNationality))
                .body("name", equalTo(newName))
                .body("salary", equalTo(newRandomSalary))
                .body("id", greaterThan(0));


        given().delete("/information/" +idNewPlayer)
                .then()
                .log().all()
                .statusCode(204);
    }
}