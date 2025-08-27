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

import java.nio.file.Path;
import java.nio.file.Paths;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrometheusTestContainerIntegrationTest {
    private static final DockerImageName PROMETHEUS_IMAGE = DockerImageName.parse("prom" +
            "/prometheus:latest");

    private GenericContainer<?> prometheusContainer;
    private PrometheusService prometheusService;

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
        PrometheusClient client = new PrometheusClient(new RestTemplate(), prometheusUrl);
        prometheusService = new PrometheusService();
        prometheusService.setPrometheusClient(client);
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

}
