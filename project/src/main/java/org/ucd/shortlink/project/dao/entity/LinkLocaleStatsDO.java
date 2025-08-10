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

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ucd.shortlink.project.common.database.BaseDO;

import java.util.Date;

/**
 * Geography Location Monitor Metrics Entity
 */
@Data
@TableName("t_link_locale_stats")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkLocaleStatsDO extends BaseDO {
    /**
     * ID
     */
    private Long id;

    /**
     * Full short url address
     */
    private String fullShortUrl;

    /**
     * Group ID
     */
    private String gid;

    /**
     * Date
     */
    private Date date;

    /**
     * Access cnt
     */
    private Integer cnt;

    /**
     * Province name
     */
    private String province;

    /**
     * City name
     */
    private String city;

    /**
     * City code
     */
    private String adcode;

    /**
     * Nation name
     */
    private String country;
}


