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

package org.ucd.shortlink.project.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Short link page response body
 */

@Data
public class ShortLinkPageRespDTO {

    private Long id;

    private String domain;

    private String shortUri;

    private String fullShortUrl;

    private String originUrl;

    private String gid;

    private Integer validateType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validDate;

    private String describe;

    private String favicon;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * History pv cnt
     */
    private Integer totalPv;

    /**
     * Today pv cnt
     */
    private Integer todayPv;

    /**
     * History uv cnt
     */
    private Integer totalUv;

    /**
     * Today uv cnt
     */
    private Integer todayUv;

    /**
     * History total uip
     */
    private Integer totalUip;

    /**
     * Today total uip
     */
    private Integer todayUIp;
}
