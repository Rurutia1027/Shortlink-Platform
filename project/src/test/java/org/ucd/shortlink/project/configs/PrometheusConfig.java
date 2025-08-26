package org.ucd.shortlink.project.configs;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.ucd.shortlink.project.prometheus.client.PrometheusClient;

@TestConfiguration
public class PrometheusConfig {
    @Bean
    public PrometheusClient prometheusClient(RestTemplate restTemplate) {
        return new PrometheusClient(restTemplate, "http://localhost:9090");
    }

    @Bean
    public RestTemplate prometheusRestTemplate() {
        return new RestTemplate();
    }
}
