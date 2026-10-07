Feature: Order Management

  Scenario Outline: Successfully place an order
    Given a product exists with 10 items in stock
    When customer orders <quantity> items using <paymentMethod>
    Then order should be CONFIRMED
    And payment should be SUCCESS
    And product stock should become <expectedStock>

  Examples:
    | quantity | paymentMethod | expectedStock |
    | 2        | CARD          | 8             |
    | 5        | UPI           | 5             |
    | 1        | CARD          | 9             |

  Scenario: Cannot place order for a product that does not exist
    Given a product does not exist
    When customer orders 2 items using CARD
    Then order should be rejected

  Scenario: Order shud fail when requested quantity exceeds available stock
    Given a product exists with 10 items in stock
    When customer orders 15 items using UPI
    Then order should be rejected