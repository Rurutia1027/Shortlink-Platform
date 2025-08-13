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

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ucd.shortlink.admin.common.biz.UserContext;
import org.ucd.shortlink.admin.common.convention.result.Result;
import org.ucd.shortlink.admin.dao.entity.GroupDO;
import org.ucd.shortlink.admin.dao.mapper.GroupMapper;
import org.ucd.shortlink.admin.dto.req.ShortLinkGroupSortRespDTO;
import org.ucd.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import org.ucd.shortlink.admin.dto.resp.ShortLinkGroupCountQueryRespDTO;
import org.ucd.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;
import org.ucd.shortlink.admin.remote.ShortLinkProjectService;
import org.ucd.shortlink.admin.remote.ShortLinkRemoteService;
import org.ucd.shortlink.admin.service.GroupService;
import org.ucd.shortlink.admin.toolkit.RandomGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * short link grouping interface implement class
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {
    private final ShortLinkProjectService shortLinkProjectService;

    @Override
    public void saveGroup(String groupName) {
        saveGroup(UserContext.getUsername(), groupName);
    }

    @Override
    public void saveGroup(String username, String groupName) {
        String gid = RandomGenerator.generateRandom();

        while (!isGidAvailable(username, gid)) {
            gid = RandomGenerator.generateRandom();
        }

        GroupDO groupDO = GroupDO.builder()
                .gid(gid)
                .sortOrder(0)
                .username(username)
                .name(groupName)
                .build();
        baseMapper.insert(groupDO);
    }

    @Override
    public List<ShortLinkGroupRespDTO> listGroup() {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .orderByDesc(GroupDO::getSortOrder, GroupDO::getUpdateTime);
        List<GroupDO> groupDOList = baseMapper.selectList(queryWrapper);
        Result<List<ShortLinkGroupCountQueryRespDTO>> listResult =
                shortLinkProjectService.listGroupShortLinkCount(groupDOList.stream().map(GroupDO::getGid).toList());


        // convert remote call queried count entity into hash map
        Map<String, Integer> shortLinkGroupCountMap = new HashMap<>();
        for (ShortLinkGroupCountQueryRespDTO countDto : listResult.getData()) {
            shortLinkGroupCountMap.put(countDto.getGid(), countDto.getShortLinkCount());
        }

        List<ShortLinkGroupRespDTO> shortLinkGroupRespDTOList = BeanUtil.copyToList(groupDOList,
                ShortLinkGroupRespDTO.class);
        shortLinkGroupRespDTOList.forEach(each -> {
            each.setShortLinkCount(shortLinkGroupCountMap.get(each.getGid()));
        });

        return shortLinkGroupRespDTOList;
    }

    @Override
    public void updateGroup(ShortLinkGroupUpdateReqDTO requestParam) {
        LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getGid, requestParam.getGid())
                .eq(GroupDO::getDelFlag, 0);
        GroupDO groupDO = new GroupDO();
        groupDO.setName(requestParam.getName());
        baseMapper.update(groupDO, updateWrapper);
    }

    @Override
    public void deleteGroup(String gid) {
        LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getDelFlag, 0);
        GroupDO groupDO = new GroupDO();
        groupDO.setDelFlag(1);
        baseMapper.update(groupDO, updateWrapper);
    }

    @Override
    public void sortGroup(List<ShortLinkGroupSortRespDTO> requestParam) {
        requestParam.forEach(item -> {
            GroupDO groupDO = GroupDO.builder()
                    .sortOrder(item.getSortOrder())
                    .build();

            LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                    .eq(GroupDO::getUsername, UserContext.getUsername())
                    .eq(GroupDO::getGid, item.getGid())
                    .eq(GroupDO::getDelFlag, 0);

            baseMapper.update(groupDO, updateWrapper);
        });
    }

    private boolean isGidAvailable(String username, String gid) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getUsername,
                        username != null ? username : UserContext.getUsername());
        GroupDO queriedGroupDO = baseMapper.selectOne(queryWrapper);
        return queriedGroupDO == null;
    }
}
