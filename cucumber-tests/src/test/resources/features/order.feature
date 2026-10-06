Feature: Order Management

  Scenario: Successfully place an order
    Given a product exists with 10 items in stock
    When customer orders 2 items using CARD
    Then order should be CONFIRMED
    And payment should be SUCCESS
    And product stock should become 8