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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromQLBuilder {
    private String metric;

    private Map<String, String> labels = new HashMap<>();
    /// e.g., sum, avg, max
    private String aggregationFunction;

    /// e.g., "instance", "job", (for sum by (instance) promQL)
    private String aggregationBy;

    /// true if we wanna query rate (rate(metric[5m])
    private boolean useRateFunction;
    /// e.g., rate(metric[5m]"5m" for rate or other range function
    private String rangeDuration;

    private Instant start;

    private Instant end;

    private String step;

    // Builder fluent addLabel
    public static class PromQLBuilderBuilder {
        public PromQLBuilderBuilder addLabel(String key, String value) {
            if (this.labels == null) {
                this.labels = new HashMap<>();
            }
            if (value != null && !value.isEmpty()) {
                this.labels.put(key, value);
            }
            return this;
        }
    }

    public String buildQuery() {
        String labelStr = labels.entrySet().stream()
                .map(e -> e.getKey() + "=\"" + e.getValue() + "\"")
                .collect(Collectors.joining(","));
        String base = metric;

        if (!labelStr.isEmpty()) {
            base = metric + "{" + labelStr + "}";
        }

        // Apply range function like rate if needed
        if (useRateFunction && rangeDuration != null) {
            base = "rate(" + base + "[" + rangeDuration + "])";
        }

        // Apply aggregation function
        if (aggregationFunction != null && !aggregationFunction.isEmpty()) {
            if (aggregationBy != null && !aggregationBy.isEmpty()) {
                base = aggregationFunction + "(" + base + ") by (" + aggregationBy + ")";
            } else {
                base = aggregationFunction + "(" + base + ")";
            }
        }

        return base;
    }

    public static String buildRangeQueryUrl(PromQLBuilder promQLBuilder,
                                            String prometheusBaseUrl) {
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
