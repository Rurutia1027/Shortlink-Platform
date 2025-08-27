package org.ucd.shortlink.project.prometheus.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PrometheusTestContainerIntegrationTest {
    private static final DockerImageName PROMETHEUS_IMAGE = DockerImageName.parse("prom" +
            "/prometheus:latest");
    private GenericContainer<?> prometheusContainer;

    @BeforeEach
    void setUp() {
        Path prometheusConfig = Paths.get("src/test/resources/prometheus-test.yml").toAbsolutePath();
        prometheusContainer = new GenericContainer<>(PROMETHEUS_IMAGE)
                .withExposedPorts(9090)
                .withFileSystemBind(prometheusConfig.toString(), "/etc/prometheus/prometheus.yml");

        prometheusContainer.start();
        String prometheusUrl = String.format("http://%s:%d",
                prometheusContainer.getHost(),
                prometheusContainer.getMappedPort(9090));
    }

    @Test
    public void testInitOK() {
        Assertions.assertNotNull(prometheusContainer.isHostAccessible());
    }


}
