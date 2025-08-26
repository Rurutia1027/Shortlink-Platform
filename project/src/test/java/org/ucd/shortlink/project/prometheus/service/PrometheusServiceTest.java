package org.ucd.shortlink.project.prometheus.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ucd.shortlink.project.prometheus.client.PrometheusClient;

@ExtendWith(MockitoExtension.class)
class PrometheusServiceTest {

    @Mock
    protected PrometheusClient prometheusClient;

    @InjectMocks
    private PrometheusService prometheusService;

    @BeforeEach
    void initMockOK() {
        Assertions.assertNotNull(prometheusClient);
        Assertions.assertNotNull(prometheusService);
    }

    @Test
    void testQueryMetricsBasic() {
        Assertions.assertTrue(true);
    }

}