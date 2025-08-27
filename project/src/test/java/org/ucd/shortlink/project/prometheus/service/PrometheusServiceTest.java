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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ucd.shortlink.project.prometheus.client.PrometheusClient;
import org.ucd.shortlink.project.prometheus.common.constant.PrometheusConstants;
import org.ucd.shortlink.project.prometheus.dto.PromQLBuilder;
import org.ucd.shortlink.project.prometheus.dto.PrometheusQueryReqDTO;
import org.ucd.shortlink.project.prometheus.dto.PrometheusQueryRespDTO;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrometheusServiceTest {

    @Mock
    protected PrometheusClient prometheusClient;

    @InjectMocks
    private PrometheusService prometheusService;

    @BeforeEach
    void initMockOK() {
        assertNotNull(prometheusClient);
        assertNotNull(prometheusService);
    }

    @Test
    void testQueryMetricsBasic() {
        PrometheusQueryReqDTO req = PrometheusQueryReqDTO.builder()
                .vertexName("node1")
                .startDate("2025-08-26T00:00:00Z")
                .endDate("2025-08-26T01:00:00Z")
                .step(PrometheusConstants.PROMETHEUS_DEFAULT_STEP)
                .build();

        List<Map<String, Object>> mockMetrics = List.of(
                Map.of("metric", Map.of("__name__", "prometheus_metric"),
                        "value", List.of(0L, 123))
        );

        when(prometheusClient.queryRange(any(PromQLBuilder.class))).thenReturn(mockMetrics);

        PrometheusQueryRespDTO resp = prometheusService.queryMetrics(req);

        assertNotNull(resp);
        assertEquals(1, resp.getMetrics().size());
        assertEquals(mockMetrics, resp.getMetrics());

        verify(prometheusClient, times(1)).queryRange(any(PromQLBuilder.class));
    }

    @Test
    void testQueryMetricWithLabelsAndAggregation() {
        PrometheusQueryReqDTO req = PrometheusQueryReqDTO.builder()
                .vertexName("node1")
                .instance("instance1")
                .job("job1")
                .aggFunc(PrometheusConstants.PROMETHEUS_AGG_FUN_COUNT)
                .aggBy(PrometheusConstants.PROMETHEUS_AGG_BY_JOB)
                .isUseRateFunc(true)
                .rangeDuration(PrometheusConstants.PROMETHEUS_RANGE_DURATION_5M)
                .startDate("2025-08-26T00:00:00Z")
                .endDate("2025-08-26T01:00:00Z")
                .step(PrometheusConstants.PROMETHEUS_DEFAULT_STEP)
                .build();

        List<Map<String, Object>> mockMetrics = List.of(
                Map.of("metric", Map.of("__name__", "prometheus_metric"),
                        "value", List.of(0L, 456)));
        when(prometheusClient.queryRange(any(PromQLBuilder.class))).thenReturn(mockMetrics);
        PrometheusQueryRespDTO resp = prometheusService.queryMetrics(req);

        assertNotNull(resp);
        assertEquals(1, resp.getMetrics().size());
        assertEquals(mockMetrics, resp.getMetrics());

        verify(prometheusClient, times(1)).queryRange(any(PromQLBuilder.class));
    }

    @Test
    void testQueryMetricsEmptyResult() {
        PrometheusQueryReqDTO req = PrometheusQueryReqDTO.builder()
                .vertexName("node2")
                .startDate("2025-08-26T00:00:00Z")
                .endDate("2025-08-26T01:00:00Z")
                .step(PrometheusConstants.PROMETHEUS_DEFAULT_STEP)
                .build();

        when(prometheusClient.queryRange(any(PromQLBuilder.class))).thenReturn(List.of());

        PrometheusQueryRespDTO resp = prometheusService.queryMetrics(req);

        assertNotNull(resp);
        assertTrue(resp.getMetrics().isEmpty());

        verify(prometheusClient, times(1)).queryRange(any(PromQLBuilder.class));
    }
}