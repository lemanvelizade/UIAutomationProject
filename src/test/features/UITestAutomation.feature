@test
Feature: UITestAutomation

  Scenario: Adding a product to the cart
    Given I open the "https://www.hepsiburada.com/" URL
    When I navigate to category
    And I filter by brand and screen size
    Then I select the most expensive product
    When I add the product to the cart
    Then I verify the product is added to the cart and the price matches


