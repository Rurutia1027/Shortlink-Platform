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

package org.ucd.shortlink.project.prometheus.dto;

import org.junit.jupiter.api.Test;
import org.ucd.shortlink.project.prometheus.common.constant.PrometheusConstants;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PromQLBuilderTest {
    @Test
    void testSimpleMetricNoLabels() {
        PromQLBuilder builder = PromQLBuilder.builder()
                .metric("prometheus_metric")
                .start(Instant.now())
                .end(Instant.now())
                .step("30s")
                .build();
        String query = builder.buildQuery();
        assertEquals("prometheus_metric", query);
    }

    @Test
    void testMetricWithSingleLabel() {
        PromQLBuilder.PromQLBuilderBuilder builder = PromQLBuilder.builder()
                .metric("prometheus_metric")
                .start(Instant.now())
                .end(Instant.now())
                .step("30s");

        builder.addLabel("vertex", "my-vertex");
        String query = builder.build().buildQuery();
        assertTrue(query.contains("vertex=\"my-vertex\""));
        assertTrue(query.startsWith("prometheus_metric{"));
    }

    @Test
    void testMetricsWithMultipleLabels() {
        PromQLBuilder.PromQLBuilderBuilder builder = PromQLBuilder.builder()
                .metric("prometheus_metric")
                .start(Instant.now())
                .end(Instant.now())
                .step("30s");

        builder.addLabel("vertex", "my-vertex");
        builder.addLabel("instance", "node-1");
        builder.addLabel("job", "aggregation");

        String query = builder.build().buildQuery();
        assertTrue(query.contains("vertex=\"my-vertex\""));
        assertTrue(query.contains("instance=\"node-1\""));
        assertTrue(query.contains("job=\"aggregation\""));
    }

    @Test
    void testMetricWithRateFunction() {
        PromQLBuilder.PromQLBuilderBuilder builder = PromQLBuilder.builder()
                .metric("prometheus_metric")
                .start(Instant.now())
                .end(Instant.now())
                .step("30s")
                .useRateFunction(true)
                .rangeDuration("5m");

        String query = builder.build().buildQuery();
        assertTrue(query.startsWith("rate("));
        assertTrue(query.contains("[5m]"));
    }

    @Test
    void testMetricWithAggregationNoBy() {
        PromQLBuilder builder = PromQLBuilder.builder()
                .metric(PrometheusConstants.PROMETHEUS_DEFAULT_METRIC_NAME)
                .start(Instant.now())
                .end(Instant.now())
                .step("30s")
                .aggregationFunction(PrometheusConstants.PROMETHEUS_AGG_FUN_SUM)
                .build();

        String query = builder.buildQuery();

        assertEquals("sum(prometheus_metric)", query);
    }

    @Test
    void testMetricWithAggregationAndBy() {
        PromQLBuilder builder = PromQLBuilder.builder()
                .metric(PrometheusConstants.PROMETHEUS_DEFAULT_METRIC_NAME)
                .start(Instant.now())
                .end(Instant.now())
                .step(PrometheusConstants.PROMETHEUS_DEFAULT_STEP)
                .aggregationFunction(PrometheusConstants.PROMETHEUS_AGG_FUN_SUM)
                .aggregationBy(PrometheusConstants.PROMETHEUS_AGG_BY_INSTANCE)
                .build();

        String query = builder.buildQuery();
        assertEquals("sum(prometheus_metric) by (instance)", query);
    }

    @Test
    void testMetricWithRateAndAggreationAndLabels() {
        PromQLBuilder builder = PromQLBuilder.builder()
                .metric(PrometheusConstants.PROMETHEUS_DEFAULT_METRIC_NAME)
                .start(Instant.now())
                .end(Instant.now())
                .step(PrometheusConstants.PROMETHEUS_DEFAULT_STEP)
                .useRateFunction(true)
                .rangeDuration(PrometheusConstants.PROMETHEUS_RANGE_DURATION_5M)
                .aggregationFunction(PrometheusConstants.PROMETHEUS_AGG_FUN_AVG)
                .aggregationBy(PrometheusConstants.PROMETHEUS_AGG_BY_JOB)
                .addLabel("vertex", "my-vertex")
                .build();

        String query = builder.buildQuery();

        // Example: avg(rate(prometheus_metric{vertex="my-vertex"}[5m])) by (job)
        assertTrue(query.startsWith("avg(rate("));
        assertTrue(query.contains("{vertex=\"my-vertex\"}"));
        assertTrue(query.endsWith(") by (job)"));
    }

    @Test
    void testBuildRangeQueryUrl() {
        Instant start = Instant.parse("2025-08-26T00:00:00Z");
        Instant end = Instant.parse("2025-08-26T01:00:00Z");

        PromQLBuilder builder = PromQLBuilder.builder()
                .metric("prometheus_metric")
                .start(start)
                .end(end)
                .step("15s")
                .build();

        String url = PromQLBuilder.buildRangeQueryUrl(builder, "http://localhost:9090");
        assertTrue(url.contains("query=prometheus_metric"));
        assertTrue(url.contains("start=" + start.getEpochSecond()));
        assertTrue(url.contains("end=" + end.getEpochSecond()));
        assertTrue(url.contains("step=15s"));
    }
}