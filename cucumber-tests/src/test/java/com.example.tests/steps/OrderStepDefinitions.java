package com.example.tests.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


import java.util.Map;
import io.restassured.http.ContentType;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderStepDefinitions {

    private Long productId;
    private Long orderId;

    @Given("a product exists with 10 items in stock")
    public void productExists() {

        Map<String, Object> request = Map.of(
                "name", "Charger",
                "price", 150,
                "stock", 10
        );

        Response response = given().contentType(ContentType.JSON)
                        .body(request)
                        .when()
                        .post("http://localhost:8082/products");

        response.then()
                .statusCode(200);

        productId = response.jsonPath()
                .getLong("id");
    }

    @When("customer orders 2 items using CARD")
    public void customerPlacesOrder() {

        Map<String, Object> request = Map.of(
                "productId", productId,
                "quantity", 2,
                "paymentMethod", "CARD"
        );

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when()
                        .post("http://localhost:8081/orders");

        response.then()
                .statusCode(200);

        orderId = response.jsonPath()
                .getLong("orderId");

        System.out.println("Created Order ID = " + orderId);
    }

    @Then("order should be CONFIRMED")
    public void orderShouldBeConfirmed() {

        System.out.println("Fetching Order ID = " + orderId);

        Response response =
                given()
                        .when()
                        .get("http://localhost:8081/orders/" + orderId);

        response.then()
                .statusCode(200);

        String status = response.jsonPath()
                .getString("status");

        assertThat(status).isEqualTo("CONFIRMED");
    }

    @Then("payment should be SUCCESS")
    public void paymentShouldBeSuccessful() {

        Response response =
                given()
                        .when()
                        .get("http://localhost:8083/payments/order/" + orderId);

        response.then()
                .statusCode(200);

        String status = response.jsonPath()
                .getString("status");

        assertThat(status).isEqualTo("SUCCESS");
    }

    @Then("product stock should become 8")
    public void stockShouldBecomeEight() {

        Response response =
                given()
                        .when()
                        .get("http://localhost:8082/products/" + productId);

        response.then()
                .statusCode(200);

        int stock = response.jsonPath()
                .getInt("stock");

        assertThat(stock).isEqualTo(9);
    }
}