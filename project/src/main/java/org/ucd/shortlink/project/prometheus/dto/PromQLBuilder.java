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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromQLBuilder {
    private String metric;
    private Map<String, String> labels;
    private String aggregationFunction;
    private String aggregationBy;
    private boolean useRateFunction;
    private String rangeDuration;
    private Instant start;
    private Instant end;
    private String step;

    // ---------------- Manual Builder ----------------
    public static class PromQLBuilderBuilder {
        private String metric;
        private Map<String, String> labels = new HashMap<>();
        private String aggregationFunction;
        private String aggregationBy;
        private boolean useRateFunction;
        private String rangeDuration;
        private Instant start;
        private Instant end;
        private String step;

        public PromQLBuilderBuilder metric(String metric) {
            this.metric = metric;
            return this;
        }

        public PromQLBuilderBuilder addLabel(String key, String value) {
            if (value != null && !value.isEmpty()) {
                this.labels.put(key, value);
            }
            return this;
        }

        public PromQLBuilderBuilder aggregationFunction(String aggregationFunction) {
            this.aggregationFunction = aggregationFunction;
            return this;
        }

        public PromQLBuilderBuilder aggregationBy(String aggregationBy) {
            this.aggregationBy = aggregationBy;
            return this;
        }

        public PromQLBuilderBuilder useRateFunction(boolean useRateFunction) {
            this.useRateFunction = useRateFunction;
            return this;
        }

        public PromQLBuilderBuilder rangeDuration(String rangeDuration) {
            this.rangeDuration = rangeDuration;
            return this;
        }

        public PromQLBuilderBuilder start(Instant start) {
            this.start = start;
            return this;
        }

        public PromQLBuilderBuilder end(Instant end) {
            this.end = end;
            return this;
        }

        public PromQLBuilderBuilder step(String step) {
            this.step = step;
            return this;
        }

        public PromQLBuilder build() {
            return new PromQLBuilder(
                    metric,
                    labels,
                    aggregationFunction,
                    aggregationBy,
                    useRateFunction,
                    rangeDuration,
                    start,
                    end,
                    step
            );
        }
    }

    public static PromQLBuilderBuilder builder() {
        return new PromQLBuilderBuilder();
    }

    // ---------------- Query Construction ----------------
    public String buildQuery() {
        String labelStr = labels.entrySet().stream()
                .map(e -> e.getKey() + "=\"" + e.getValue() + "\"")
                .collect(Collectors.joining(","));
        String base = metric;

        if (!labelStr.isEmpty()) {
            base = metric + "{" + labelStr + "}";
        }

        if (useRateFunction && rangeDuration != null) {
            base = "rate(" + base + "[" + rangeDuration + "])";
        }

        if (aggregationFunction != null && !aggregationFunction.isEmpty()) {
            if (aggregationBy != null && !aggregationBy.isEmpty()) {
                base = aggregationFunction + "(" + base + ") by (" + aggregationBy + ")";
            } else {
                base = aggregationFunction + "(" + base + ")";
            }
        }

        return base;
    }

    public static String buildRangeQueryUrl(PromQLBuilder promQLBuilder, String prometheusBaseUrl) {
        String query = URLEncoder.encode(promQLBuilder.buildQuery(), StandardCharsets.UTF_8);
        return String.format("%s/api/v1/query_range?query=%s&start=%d&end=%d&step=%s",
                prometheusBaseUrl,
                query,
                promQLBuilder.getStart().getEpochSecond(),
                promQLBuilder.getEnd().getEpochSecond(),
                promQLBuilder.getStep()
        );
    }
}