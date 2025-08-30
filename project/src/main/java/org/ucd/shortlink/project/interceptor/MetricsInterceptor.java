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

package org.ucd.shortlink.project.interceptor;

import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Random;

@Component
public class MetricsInterceptor implements HandlerInterceptor {
    private static final Counter requests = Counter.build()
            .name("shortlink_project_requests_total")
            .help("Total Requests.")
            .labelNames("job")
            .register();

    private static final Histogram requestLatency = Histogram.build()
            .name("shortlink_request_latency_seconds")
            .help("Request latency in seconds.")
            .labelNames("job")
            .register();

    private final Random random = new Random();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jobName = request.getHeader("X-Job-Name");
        if (jobName == null) {
            jobName = "default";
        }

        requests.labels(jobName).inc();
        request.setAttribute("timer", requestLatency.labels(jobName).startTimer());

        // simulate process some work here
        try {
            Thread.sleep(200 + random.nextInt(200));
        } catch (InterruptedException ex) {
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Histogram.Timer timer = (Histogram.Timer) request.getAttribute("timer");
        if (timer != null) {
            timer.observeDuration();
        }
    }
}
