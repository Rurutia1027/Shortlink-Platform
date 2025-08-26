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

package org.ucd.shortlink.project.prometheus.client;

import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.ucd.shortlink.project.prometheus.dto.PromQLBuilder;

import java.util.List;
import java.util.Map;

@Data
@Component
public class PrometheusClient {
    private final RestTemplate restTemplate;

    private final String prometheusBaseUrl;

    public PrometheusClient(
            @Qualifier("prometheusRestTemplate") RestTemplate restTemplate,
            @Qualifier("prometheusBaseUrl") String prometheusBaseUrl
    ) {
        this.restTemplate = restTemplate;
        this.prometheusBaseUrl = prometheusBaseUrl;
    }

    public List<Map<String, Object>> queryRange(PromQLBuilder promQLBuilder) {
        String promql = promQLBuilder.buildQuery();
        Long startTs = promQLBuilder.getStart().getEpochSecond();
        Long endTs = promQLBuilder.getEnd().getEpochSecond();
        String step = promQLBuilder.getStep();

        String url = String.format(
                "%s/api/v1/query_range?query=%s&start=%s&end=%s&step=%s",
                prometheusBaseUrl, promql, startTs, endTs, step
        );

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        return (List<Map<String, Object>>) ((Map<String, Object>) response.get("data")).get("result");
    }

}
