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

package org.ucd.shortlink.project.configs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.ucd.shortlink.project.config.WebConfig;
import org.ucd.shortlink.project.interceptor.MetricsInterceptor;
import org.ucd.shortlink.project.prometheus.client.PrometheusClient;
import org.ucd.shortlink.project.prometheus.service.PrometheusService;

@TestConfiguration
public class PrometheusMetricTestConfig {

    @Bean("testPrometheusClient")
    public PrometheusClient prometheusClient(@Qualifier("testRestTemplate") RestTemplate restTemplate,
                                             @Value("${prometheus.url}") String prometheusUrl) {
        return new PrometheusClient(restTemplate, prometheusUrl);
    }

    @Bean("testPrometheusService")
    public PrometheusService prometheusService(@Qualifier("testPrometheusClient") PrometheusClient client) {
        PrometheusService prometheusService = new PrometheusService();
        prometheusService.setPrometheusClient(client);
        return prometheusService;
    }

    @Bean("testMetricsInterceptor")
    public MetricsInterceptor metricsInterceptor() {
        return new MetricsInterceptor();
    }

    @Bean("testWebConfig")
    public WebConfig webConfig(@Qualifier("testMetricsInterceptor") MetricsInterceptor interceptor) {
        return new WebConfig(interceptor);
    }

    @Bean("testRestTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
