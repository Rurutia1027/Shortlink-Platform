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

package org.ucd.shortlink.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ucd.shortlink.admin.common.biz.UserContext;
import org.ucd.shortlink.admin.common.convention.exception.ServiceException;
import org.ucd.shortlink.admin.common.convention.result.Result;
import org.ucd.shortlink.admin.dao.entity.GroupDO;
import org.ucd.shortlink.admin.dao.mapper.GroupMapper;
import org.ucd.shortlink.admin.remote.ShortLinkProjectService;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.ucd.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.ucd.shortlink.admin.service.RecycleBinService;

import java.util.List;

/**
 * Short Link Recycle interface implementor
 */
@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl implements RecycleBinService {
    private final GroupMapper groupMapper;
    private final ShortLinkProjectService shortLinkProjectService;

    /**
     * Paging query recycled short link
     *
     * @param requestParam paging queried recycled short link parameters
     * @return
     */

    @Override
    public Result<Page<ShortLinkPageRespDTO>> pageRecycledShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0);

        List<GroupDO> groupDOList = groupMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(groupDOList)) {
            throw new ServiceException("Current user [" + UserContext.getUsername() + "] " +
                    "does not have any group info!");
        }

        requestParam.setGidList(groupDOList.stream().map(GroupDO::getGid).toList());
        return shortLinkProjectService.pageRecycleBinShortLink(requestParam);
    }
}