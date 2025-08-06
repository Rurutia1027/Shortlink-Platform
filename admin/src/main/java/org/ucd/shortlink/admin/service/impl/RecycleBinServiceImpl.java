package org.ucd.shortlink.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ucd.shortlink.admin.common.biz.UserContext;
import org.ucd.shortlink.admin.common.convention.exception.ServiceException;
import org.ucd.shortlink.admin.common.convention.result.Result;
import org.ucd.shortlink.admin.dao.entity.GroupDO;
import org.ucd.shortlink.admin.dao.mapper.GroupMapper;
import org.ucd.shortlink.admin.remote.ShortLinkRemoteService;
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

    // TODO: Refactor into SpringCloud Feign invocation
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };

    /**
     * Paging query recycled short link
     *
     * @param requestParam paging queried recycled short link parameters
     * @return
     */

    @Override
    public Result<IPage<ShortLinkPageRespDTO>> pageRecycledShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0);

        List<GroupDO> groupDOList = groupMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(groupDOList)) {
            throw new ServiceException("Current user [" + UserContext.getUsername() + "] " +
                    "does not have any group info!");
        }

        requestParam.setGidList(groupDOList.stream().map(GroupDO::getGid).toList());
        return shortLinkRemoteService.pageRecycledShortLink(requestParam);
    }
}