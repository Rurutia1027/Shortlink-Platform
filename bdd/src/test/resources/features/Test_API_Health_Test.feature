Feature: API Health Check
  Scenario: Call /api/short-link/admin/v1/tests
    Given the API base URL is "http://localhost:8002"
    When I send a GET request to "/api/short-link/admin/v1/tests"
    Then the response status code should be 200
    And the response body should contain "Test OK"