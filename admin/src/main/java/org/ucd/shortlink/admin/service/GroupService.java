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

package org.ucd.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.ucd.shortlink.admin.dao.entity.GroupDO;
import org.ucd.shortlink.admin.dto.req.ShortLinkGroupSortRespDTO;
import org.ucd.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import org.ucd.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;

import java.util.List;

/**
 * short link grouping service
 */
public interface GroupService extends IService<GroupDO> {
    /**
     * Add a short link grouping entity
     *
     * @param groupName short link group name
     */
    void saveGroup(String groupName);


    /**
     * Add a short link grouping entity
     *
     * @param username
     *
     * @param groupName short link group name
     */
    void saveGroup(String username, String groupName);

    /**
     * Query short link group by username
     *
     * @return queried user short link group item collection
     */
    List<ShortLinkGroupRespDTO> listGroup();

    /**
     * Update short link group record
     *
     * @param requestParam group name
     */
    void updateGroup(ShortLinkGroupUpdateReqDTO requestParam);

    /**
     * Delete short link group via gid
     *
     * @param gid id of group
     */
    void deleteGroup(String gid);

    /**
     * Short link group sort
     *
     * @param requestParam sort param
     */
    void sortGroup(List<ShortLinkGroupSortRespDTO> requestParam);
}
