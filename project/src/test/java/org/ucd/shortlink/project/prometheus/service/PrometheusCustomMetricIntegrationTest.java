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

import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import org.ucd.shortlink.project.configs.PrometheusMetricTestConfig;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PrometheusMetricTestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrometheusCustomMetricIntegrationTest {
    private static final DockerImageName PROMETHEUS_IMAGE = DockerImageName.parse("prom" +
            "/prometheus:latest");

    private static Network network;

    @Container
    private static GenericContainer<?> prometheusContainer;

    @Autowired
    private PrometheusService prometheusService;

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("prometheus.url", () ->
                String.format("http://%s:%d",
                        prometheusContainer.getHost(),
                        prometheusContainer.getMappedPort(9090))
        );
    }

    static {
        network = Network.newNetwork();
        Path prometheusConfig = null;
        try {
            prometheusConfig = Files.createTempFile("prometheus-test", ".yml");
            FileWriter writer = new FileWriter(prometheusConfig.toFile());

            writer.write("global:\n" +
                    "  scrape_interval: 2s\n" +
                    "scrape_configs:\n" +
                    "  - job_name: 'shortlink-project'\n" +
                    "    static_configs:\n" +
                    "      - targets: ['host.testcontainers.internal:9100']\n");
        } catch (Exception e) {
        }
        prometheusContainer = new GenericContainer<>(PROMETHEUS_IMAGE)
                .withNetwork(network)
                .withFileSystemBind(prometheusConfig.toString(), "/etc/prometheus/prometheus.yml")
                .withExposedPorts(9090)
                .waitingFor(Wait.forHttp("/-/ready").forStatusCode(200));
        prometheusContainer.start();
    }

    @Test
    public void initOk() {
        Assertions.assertNotNull(prometheusContainer);
        Assertions.assertNotNull(prometheusService);
        Assertions.assertNotNull(prometheusService.getPrometheusClient());
    }

    // -- define prometheus metrics
    private static final Counter requests = Counter.build()
            .name("shortlink_project_requests_total")
            .help("Total requests for shortlink project")
            .labelNames("job")
            .register();

    private static final Histogram requestLatency = Histogram.build()
            .name("shortlink_request_latency_seconds")
            .help("Request latency in seconds")
            .labelNames("job")
            .register();


}
