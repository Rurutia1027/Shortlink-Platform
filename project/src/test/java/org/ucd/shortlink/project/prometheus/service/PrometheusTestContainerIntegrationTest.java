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
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrometheusTestContainerIntegrationTest {
    private static final DockerImageName PROMETHEUS_IMAGE = DockerImageName.parse("prom" +
            "/prometheus:latest");

    private GenericContainer<?> prometheusContainer;
    private PrometheusService prometheusService;
    private PrometheusClient prometheusClient;

    @BeforeAll
    void setUp() {
        Path prometheusConfig = Paths.get("src/test/resources/prometheus-test.yml").toAbsolutePath();
        prometheusContainer = new GenericContainer<>(PROMETHEUS_IMAGE)
                .withExposedPorts(9090)
                .withFileSystemBind(prometheusConfig.toString(), "/etc/prometheus/prometheus.yml");

        prometheusContainer.start();
        String prometheusUrl = String.format("http://%s:%d",
                prometheusContainer.getHost(),
                prometheusContainer.getMappedPort(9090));
        System.out.println("we got our test context available prometheus endpoint " + prometheusUrl);
        prometheusService = new PrometheusService();
        prometheusClient = new PrometheusClient(new RestTemplate(), prometheusUrl);
        prometheusService.setPrometheusClient(prometheusClient);
    }

    @AfterAll
    void stopPrometheus() {
        if (prometheusContainer != null) {
            prometheusContainer.stop();
        }
    }


    @Test
    public void testInitOK() {
        Assertions.assertNotNull(prometheusContainer.isHostAccessible());
        Assertions.assertNotNull(prometheusService);
        Assertions.assertNotNull(prometheusService.getPrometheusClient());
    }

    @Test
    public void testQueryPrometheusUpJobs() {
        Instant now = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .withZone(ZoneOffset.UTC);
        PrometheusQueryReqDTO req = PrometheusQueryReqDTO
                .builder()
                .metricName(PrometheusConstants.PROMETHEUS_UP_METRIC_NAME)
                .startDate(formatter.format(now))
                .endDate(formatter.format(now.plusSeconds(3600)))
                .step(PrometheusConstants.PROMETHEUS_DEFAULT_STEP)
                .build();

        PrometheusRespDTO resp = prometheusService.queryPrometheusMetric(req);
        Assertions.assertNotNull(resp);
    }
}
