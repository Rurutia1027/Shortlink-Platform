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

package org.ucd.shortlink.project.micrometer.common.constant;

public class MicrometerMetricsConstatns {
    public static final String JOB_NAME_SHORTLINK_PROJECT = "shortlink-service";

    public static final String METRIC_NAME_SHORTLINK_PAGE_VIEWS_TOTAL =
            "shortlink_page_views_total";

    public static final String METRIC_NAME_SHORTLINK_UNIQUE_USERS_TOTAL =
            "shortlink_unique_users_total";

    public static final String METRIC_NAME_SHORTLINK_UNIQUE_IP_TOTAL =
            "shortlink_unique_ip_total";

    public static final String METRIC_NAME_SHORTLINK_IP_HITS_TOTAL = "shortlink_ip_hits_total";

    public static final String METRIC_NAME_SHORTLINK_BROWSER_TYPE_TOTAL =
            "shortlink_browser_type_total";

    public static final String METRIC_NAME_SHORTLINK_OS_TYPE_TOTAL = "shortlink_os_type_total";
    public static final String METRIC_NAME_SHORTLINK_DEVICE_TYPE_TOTAL =
            "shortlink_device_type_total";
}
