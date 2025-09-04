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

package org.ucd.shortlink.project.common.constant;

/**
 * Redis key constant class
 */
public class RedisKeyConstant {
    /**
     * Short link redirection cache key
     */
    public static final String REDIRECT_SHORT_LINK_KEY = "short-link_redirect_%s";

    /**
     * Short link blank redirection address cache key
     */
    public static final String REDIRECT_IS_BLANK_SHORT_LINK_KEY = "short" +
            "-link_is_blank_redirect_%s";

    /**
     * Short link redirection distributed lock (via Redis)
     */
    public static final String LOCK_REDIRECT_SHORT_LINK_KEY = "short-link_lock_redirect_%s";

    /**
     * Short link redis cache uv counter key
     */
    public static final String REDIS_KEY_SHORT_LINK_RESTORE_UV_KEY = "short-link:stats:uv";
    public static final String REDIS_KEY_STATS_UIP = "short-link:stats:uip";
}
