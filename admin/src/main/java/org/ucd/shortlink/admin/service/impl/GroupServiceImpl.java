package org.ucd.shortlink.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ucd.shortlink.admin.dao.entity.GroupDO;
import org.ucd.shortlink.admin.dao.mapper.GroupMapper;
import org.ucd.shortlink.admin.service.GroupService;
import org.ucd.shortlink.admin.toolkit.RandomGenerator;

/**
 * short link grouping interface implement class
 */
@Slf4j
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {
    @Override
    public void saveGroup(String groupName) {
        String gid = RandomGenerator.generateRandom();

        while (!isGidAvailable(gid)) {
            gid = RandomGenerator.generateRandom();
        }

        GroupDO groupDO = GroupDO.builder().name(groupName)
                .gid(gid).build();
        baseMapper.insert(groupDO);
    }

    private boolean isGidAvailable(String gid) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                // TODO: fetch username from context
                .eq(GroupDO::getUsername, null);
        GroupDO queriedGroupDO = baseMapper.selectOne(queryWrapper);
        return queriedGroupDO == null;
    }
}
