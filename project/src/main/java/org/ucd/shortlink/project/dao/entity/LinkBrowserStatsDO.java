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
 * Browser monitor metric entity
 */
@Data
@TableName("t_link_browser_stats")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkBrowserStatsDO extends BaseDO {
    /**
     * id
     */
    private Long id;

    /**
     * full short url
     */
    private String fullShortUrl;

    /**
     * group ID
     */
    private String gid;

    /**
     * date
     */
    private Date date;

    /**
     * access cnt
     */
    private Integer cnt;

    /**
     * browser info
     */
    private String browser;
}
