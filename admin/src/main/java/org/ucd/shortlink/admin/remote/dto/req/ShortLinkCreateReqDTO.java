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

package org.ucd.shortlink.admin.remote.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Short link creation request object
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkCreateReqDTO {
    /**
     * Domain name
     */
    private String domain;

    /**
     * Original URL address
     */
    private String originUrl;

    /**
     * Short Link Group ID
     */
    private String gid;

    /**
     * Short Link create type: 0: interface request, 1: console command request
     */
    private Integer createdType;

    /**
     * Validate Date Type: 0: permanent, 1: customize
     */
    private Integer validDateType;

    /**
     * Short link validate date range
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validDate;


    /**
     * Description
     */
    private String describe;
}
