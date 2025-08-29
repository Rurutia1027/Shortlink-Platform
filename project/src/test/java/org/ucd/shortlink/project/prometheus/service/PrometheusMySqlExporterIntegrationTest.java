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

package org.ucd.shortlink.project.prometheus.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import org.ucd.shortlink.project.prometheus.common.constant.PrometheusConstants;
import org.ucd.shortlink.project.prometheus.dto.PrometheusQueryReqDTO;
import org.ucd.shortlink.project.prometheus.dto.PrometheusQueryRespDTO;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrometheusMySqlExporterIntegrationTest {
    private static final DockerImageName PROMETHEUS_IMAGE =  DockerImageName.parse("prom" +
            "/prometheus:latest");
    private static final DockerImageName MYSQL_IMAGE = DockerImageName.parse("mysql:8.0");
    private static final DockerImageName MYSQL_EXPORTER_IMAGE = DockerImageName.parse("prom/mysqld-exporter:v0.14.0");

    // network shared among all containers
    private static Network network;
    private static MySQLContainer<?> mySQLContainer;
    private static GenericContainer<?> mysqlExporterContainer;
    private static GenericContainer<?> prometheusContainer;

    @BeforeAll
    public static void setupContainers() {
        // create shared network instance
        network = Network.newNetwork();

        mySQLContainer = new MySQLContainer<>(MYSQL_IMAGE)
                .withDatabaseName("prometheusdb")
                .withUsername("testuser")
                .withPassword("testpswd")
                .withNetwork(network)
                .withNetworkAliases("mysql")
                .waitingFor(Wait.forListeningPort());

        mySQLContainer.start();

        // mysql exporter
        mysqlExporterContainer = new GenericContainer<>(MYSQL_EXPORTER_IMAGE)
                .withNetwork(network)
                .dependsOn(mySQLContainer)
                .withEnv("DATA_SOURCE_NAME", "testuser:testpswd@(mysql:3306)/")
                .withExposedPorts(9104)
                .waitingFor(Wait.forHttp("/metrics").forStatusCode(200));
        mysqlExporterContainer.start();

        // prometheus
        Path prometheusConfig = Paths.get("src/test/resources/prometheus-test.yml").toAbsolutePath();
        prometheusContainer = new GenericContainer<>(PROMETHEUS_IMAGE)
                .withNetwork(network)
                .dependsOn(mysqlExporterContainer)
                .withFileSystemBind(prometheusConfig.toString(), "/etc/prometheus/prometheus.yml")
                .withExposedPorts(9090)
                .waitingFor(Wait.forHttp("/-/ready").forStatusCode(200));

        prometheusContainer.start();
    }

    @Test
    public void testInitOK() {
        Assertions.assertNotNull(prometheusContainer);
        Assertions.assertNotNull(mysqlExporterContainer);
        Assertions.assertNotNull(mySQLContainer);
    }

    @AfterAll
    public void tearDown() {
        if (mysqlExporterContainer != null) {
            mysqlExporterContainer.stop();
        }

        if (mySQLContainer != null) {
            mySQLContainer.stop();
        }

        if (prometheusContainer != null) {
            prometheusContainer.stop();
        }

        if (network != null) {
            network.close();
        }
    }

    // @Test
    public void testQueryPrometheusUpJobs() {
        // Example: instantiate your controller or service directly
        PrometheusService prometheusService = new PrometheusService();

        Instant now = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .withZone(ZoneOffset.UTC);

        PrometheusQueryReqDTO req = PrometheusQueryReqDTO.builder()
                .metricName(PrometheusConstants.PROMETHEUS_UP_METRIC_NAME)
                .startDate(formatter.format(now))
                .endDate(formatter.format(now.plusSeconds(3600)))
                .step(PrometheusConstants.PROMETHEUS_DEFAULT_STEP)
                .build();

        PrometheusQueryRespDTO resp = prometheusService.queryMetrics(req);
        Assertions.assertNotNull(resp);
        if (!resp.getMetrics().isEmpty()) {
            Assertions.assertTrue(resp.getMetrics().get(0).keySet().contains("metric")
                    && resp.getMetrics().get(0).keySet().contains("values"));
        }
    }
}
