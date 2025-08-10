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

package org.ucd.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ucd.shortlink.project.common.database.BaseDO;

import java.util.Date;

/**
 * Short link entity
 */

@Data
@Builder
@TableName("t_link")
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkDO extends BaseDO {
    /**
     * Short link ID
     */
    private Long id;

    /**
     * Domain name
     */
    private String domain;

    /**
     * Short URI
     */
    private String shortUri;

    /**
     * Full Short URL
     */
    private String fullShortUrl;

    /**
     * Original URL address
     */
    private String originUrl;

    /**
     * Short Link click statistic number
     */
    private Integer clickNum;

    /**
     * Short Link Group ID
     */
    private String gid;

    /**
     * Enable Status: 0: enabled, 1: non-enabled
     */
    private Integer enableStatus;

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
    private Date validDate;


    /**
     * Description
     */
    @TableField("`describe`")
    private String describe;


    /**
     * Website favicon
     */
    private String favicon;

    /**
     * History pv
     */
    private Integer totalPv;

    /**
     * History Uv
     */
    private Integer totalUv;

    /**
     * History Uip
     */
    private Integer totalUip;
}
