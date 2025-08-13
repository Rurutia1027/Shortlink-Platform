/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ucd.shortlink.bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApiHealthCheckSteps {

    private String baseUrl;
    private String endpoint;
    private int responseCode;
    private String responseBody;

    @Given("the API base URL is {string}")
    public void setApiBaseUrl(String url) {
        this.baseUrl = url;
    }

    @When("I send a GET request to {string}")
    public void sendGetRequest(String endpoint) throws IOException {
        this.endpoint = endpoint;
        URL url = new URL(baseUrl + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        this.responseCode = conn.getResponseCode();

        Scanner scanner = new Scanner(conn.getInputStream());
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNext()) {
            sb.append(scanner.nextLine());
        }
        scanner.close();
        this.responseBody = sb.toString();
    }

    @Then("the response status code should be {int}")
    public void verifyResponseStatusCode(int expectedCode) {
        assertEquals(expectedCode, this.responseCode);
    }

    @Then("the response body should contain {string}")
    public void verifyResponseBodyContains(String expectedText) {
        assertTrue(this.responseBody.contains(expectedText));
    }
}