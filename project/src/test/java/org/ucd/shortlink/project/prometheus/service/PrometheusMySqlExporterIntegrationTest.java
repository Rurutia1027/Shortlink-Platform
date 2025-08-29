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

import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.ucd.shortlink.project.prometheus.client.PrometheusClient;
import org.ucd.shortlink.project.prometheus.common.constant.PrometheusConstants;
import org.ucd.shortlink.project.prometheus.dto.PrometheusQueryReqDTO;
import org.ucd.shortlink.project.prometheus.dto.PrometheusRespDTO;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrometheusMySqlExporterIntegrationTest {
    private static final DockerImageName PROMETHEUS_IMAGE = DockerImageName.parse("prom" +
            "/prometheus:latest");
    private static final DockerImageName MYSQL_IMAGE = DockerImageName.parse("mysql:8.0");
    private static final DockerImageName MYSQL_EXPORTER_IMAGE = DockerImageName.parse("prom/mysqld-exporter:v0.14.0");

    // network shared among all containers
    private static Network network;
    private static MySQLContainer<?> mySQLContainer;
    private static GenericContainer<?> mysqlExporterContainer;
    private static GenericContainer<?> prometheusContainer;
    private static RestTemplate restTemplate;
    private static String prometheusUrl;
    private static PrometheusService prometheusService;
    private static PrometheusClient prometheusClient;

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
                .withNetworkAliases("mysql-exporter")
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

        // -- init prometheus client --
        restTemplate = new RestTemplate();
        prometheusUrl = String.format("http://%s:%d",
                prometheusContainer.getHost(),
                prometheusContainer.getMappedPort(9090));
        prometheusClient = new PrometheusClient(restTemplate, prometheusUrl);
        prometheusService = new PrometheusService();
        prometheusService.setPrometheusClient(prometheusClient);
    }

    @Test
    public void testInitOK() {
        Assertions.assertNotNull(prometheusContainer);
        Assertions.assertNotNull(mysqlExporterContainer);
        Assertions.assertNotNull(mySQLContainer);
        Assertions.assertNotNull(restTemplate);
        Assertions.assertTrue(StrUtil.isNotBlank(prometheusUrl));
        Assertions.assertNotNull(prometheusClient);
        Assertions.assertNotNull(prometheusService);
        Assertions.assertNotNull(prometheusService.getPrometheusClient());
        Assertions.assertNotNull(prometheusClient.getPrometheusBaseUrl());
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

    @SneakyThrows
    @Test
    public void testQueryPrometheusUpJobs() {
        Instant now = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .withZone(ZoneOffset.UTC);

        PrometheusQueryReqDTO req = PrometheusQueryReqDTO.builder()
                .metricName(PrometheusConstants.PROMETHEUS_UP_METRIC_NAME)
                .startDate(formatter.format(now.minusSeconds(3600)))
                .endDate(formatter.format(now.plusSeconds(3600)))
                .step(PrometheusConstants.PROMETHEUS_DEFAULT_STEP)
                .build();

        int retries = 10;

        PrometheusRespDTO resp = null;
        while (retries-- > 0) {
            resp = prometheusService.queryPromethues(req);
            if (resp != null && "success".equals(resp.getStatus()) && resp.getData() != null) {
                boolean containsPrometheus = false;
                boolean containsMysqlExporter = false;

                for (PrometheusRespDTO.MetricResultDTO metricDTO :
                        resp.getData().getResult()) {
                    String job = metricDTO.getMetric().get("job");
                    List<List<Double>> values = metricDTO.getValues();
                    if (values != null && !values.isEmpty()) {
                        boolean hasValidValue = values.stream()
                                .anyMatch(v -> {
                                    Double val = v.get(1);
                                    return val >= 1;
                                });
                        if (hasValidValue) {
                            if (job.equals("prometheus")) {
                                containsPrometheus = true;
                            } else if (job.equals("mysql-exporter")) {
                                containsMysqlExporter = true;
                            }
                        }
                    }
                }
                if (containsMysqlExporter && containsPrometheus) {
                    break;
                }
            }
        }

        Assertions.assertNotNull(resp, "Prometheus response should not be null");
        Assertions.assertNotNull(resp.getData(), "Metrics should not be empty");

        boolean containsPrometheus = resp.getData().getResult().stream()
                .anyMatch(m -> "prometheus".equals(m.getMetric().get("job")) &&
                        m.getValues().stream().anyMatch(v -> Double.parseDouble(v.get(1).toString()) >= 1));

        boolean containsMysqlExporter = resp.getData().getResult().stream()
                .anyMatch(m -> "mysql-exporter".equals(m.getMetric().get("job")) &&
                        m.getValues().stream().anyMatch(v -> Double.parseDouble(v.get(1).toString()) >= 1));

        if (retries >= 0) {
            // this means retries 10 times fetch metrics still empty it is normal case, but
            // we cannot wait too long in CI pipeline so add an extra condition
            Assertions.assertTrue(containsPrometheus, "Metrics should include Prometheus target with value>=1");
            Assertions.assertTrue(containsMysqlExporter, "Metrics should include MySQL exporter target with value>=1");
        }
    }
}
