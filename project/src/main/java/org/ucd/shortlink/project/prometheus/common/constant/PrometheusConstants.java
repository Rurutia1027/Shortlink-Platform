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

package org.ucd.shortlink.project.prometheus.common.constant;

public class PrometheusConstants {
    public static final String PROMETHEUS_DEFAULT_METRIC_NAME = "prometheus_metric";
    public static final String PROMETHEUS_UP_METRIC_NAME = "up";

    public static final String PROMETHEUS_RANGE_DURATION_DEFAULT = "1m";

    public static final String PROMETHEUS_RANGE_DURATION_1M = "1m";

    public static final String PROMETHEUS_RANGE_DURATION_3M = "3m";

    public static final String PROMETHEUS_RANGE_DURATION_5M = "5m";

    public static final String PROMETHEUS_RANGE_DURATION_20M = "10m";

    public static final String PROMETHEUS_RANGE_DURATION_15M = "15m";

    public static final String PROMETHEUS_AGG_FUN_SUM = "sum";

    public static final String PROMETHEUS_AGG_FUN_MIN = "min";

    public static final String PROMETHEUS_AGG_FUN_MAX = "max";

    public static final String PROMETHEUS_AGG_FUN_AVG = "avg";

    public static final String PROMETHEUS_AGG_FUN_STDDEV = "stddev";

    public static final String PROMETHEUS_AGG_FUN_STDVAR = "stdvar";

    public static final String PROMETHEUS_AGG_FUN_COUNT = "count";

    public static final String PROMETHEUS_DEFAULT_STEP = "30s";

    public static final String PROMETHEUS_AGG_BY_INSTANCE = "instance";

    public static final String PROMETHEUS_AGG_BY_JOB = "job";
}
