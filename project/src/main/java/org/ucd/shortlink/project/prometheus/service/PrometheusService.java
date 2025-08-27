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

import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ucd.shortlink.project.prometheus.client.PrometheusClient;
import org.ucd.shortlink.project.prometheus.dto.PromQLBuilder;
import org.ucd.shortlink.project.prometheus.dto.PrometheusQueryReqDTO;
import org.ucd.shortlink.project.prometheus.dto.PrometheusQueryRespDTO;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PrometheusService {
    private PrometheusClient prometheusClient;

    public PrometheusQueryRespDTO queryMetrics(PrometheusQueryReqDTO requestParam) {
        // Initialize Builder
        PromQLBuilder.PromQLBuilderBuilder builder = PromQLBuilder.builder()
                .metric("prometheus_metric")
                .start(DateUtil.parse(requestParam.getStartDate()).toInstant())
                .end(DateUtil.parse(requestParam.getEndDate()).toInstant())
                .step(requestParam.getStep());


        // Mandatory label
        builder.addLabel("vertex", requestParam.getVertexName());

        // Optional labels
        if (requestParam.getInstance() != null && !requestParam.getInstance().isEmpty()) {
            builder.addLabel("instance", requestParam.getInstance());
        }

        // Optional labels
        if (requestParam.getJob() != null && !requestParam.getJob().isEmpty()) {
            builder.addLabel("job", requestParam.getJob());
        }

        // Optional aggregation function
        if (requestParam.getAggFunc() != null && !requestParam.getAggFunc().isEmpty()) {
            builder.aggregationFunction(requestParam.getAggFunc());
            if (requestParam.getAggBy() != null && !requestParam.getAggBy().isEmpty()) {
                builder.aggregationBy(requestParam.getAggBy());
            }
        }

        // Optional rate function
        if (requestParam.isUseRateFunc()) {
            builder.useRateFunction(true);
            if (requestParam.getRangeDuration() != null) {
                builder.rangeDuration(requestParam.getRangeDuration());
            } else {
                builder.rangeDuration("5m");
            }
        }

        // Build PromQL string
        PromQLBuilder promQLBuilder = builder.build();
        List<Map<String, Object>> result = prometheusClient.queryRange(promQLBuilder);
        PrometheusQueryRespDTO respDTO = PrometheusQueryRespDTO.builder()
                .metrics(result).build();
        return respDTO;
    }
}
