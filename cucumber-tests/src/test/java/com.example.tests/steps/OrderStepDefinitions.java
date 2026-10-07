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

public class OrderStepDefinitions {

    private Long productId;
    private Long orderId;
    private Response response;


    @Given("a product exists with {int} items in stock")
    public void productExists(int quantity) {

        Map<String, Object> request = Map.of(
                "name", "Charger",
                "price", 150,
                "stock", quantity
        );

        response =
                given()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when()
                        .post("http://localhost:8082/products");

        response.then()
                .statusCode(200);

        productId = response.jsonPath()
                .getLong("id");
    }


    @Given("a product does not exist")
    public void productDoesNotExist() {

        productId = 999999L;
    }


    @When("customer orders {int} items using {word}")
    public void customerPlacesOrder(int quantity, String paymentMethod) {

        Map<String, Object> request = Map.of(
                "productId", productId,
                "quantity", quantity,
                "paymentMethod", paymentMethod
        );

        response =
                given()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when()
                        .post("http://localhost:8081/orders");
    }


    @Then("order should be CONFIRMED")
    public void orderShouldBeConfirmed() {

        orderId = response.jsonPath()
                .getLong("orderId");

        Response orderResponse =
                given()
                        .when()
                        .get("http://localhost:8081/orders/" + orderId);

        orderResponse.then()
                .statusCode(200);

        String status =
                orderResponse.jsonPath()
                        .getString("status");

        assertThat(status)
                .isEqualTo("CONFIRMED");
    }

    @Then("product stock should become {int}")
    public void productStockShouldBecome(int expectedStock) {

        Response stockResponse =
                given()
                        .when()
                        .get("http://localhost:8082/products/" + productId);

        stockResponse.then()
                .statusCode(200);

        int actualStock =
                stockResponse.jsonPath()
                        .getInt("stock");

        assertThat(actualStock)
                .isEqualTo(expectedStock);
    }

    @Then("payment should be SUCCESS")
    public void paymentShouldBeSuccessful() {

        Response paymentResponse =
                given()
                        .when()
                        .get("http://localhost:8083/payments/order/" + orderId);

        paymentResponse.then()
                .statusCode(200);

        String status =
                paymentResponse.jsonPath()
                        .getString("status");

        assertThat(status)
                .isEqualTo("SUCCESS");
    }

    @Then("order should be rejected")
    public void orderShouldBeRejected() {

        response.then()
                .statusCode(500);
    }
}