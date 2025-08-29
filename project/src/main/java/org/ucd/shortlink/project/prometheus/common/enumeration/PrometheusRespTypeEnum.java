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

package org.ucd.shortlink.project.prometheus.common.enumeration;

/**
 * Enum representing Prometheus query response types.
 * See: https://prometheus.io/docs/prometheus/latest/querying/api/
 */
public enum PrometheusRespTypeEnum {
    MATRIX("matrix"),
    VECTOR("vector"),
    SCALAR("scalar"),
    STRING("string");

    private final String type;

    PrometheusRespTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    /**
     * Resolve enum from raw Prometheus response string.
     *
     * @param type Prometheus "resultType" string
     * @return PrometheusRespType
     * @throws IllegalArgumentException if unknown type
     */
    public static PrometheusRespTypeEnum from(String type) {
        for (PrometheusRespTypeEnum respType : values()) {
            if (respType.type.equalsIgnoreCase(type)) {
                return respType;
            }
        }
        throw new IllegalArgumentException("Unknown Prometheus response type: " + type);
    }
}