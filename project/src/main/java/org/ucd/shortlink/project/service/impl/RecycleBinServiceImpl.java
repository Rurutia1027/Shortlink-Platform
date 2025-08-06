package org.ucd.shortlink.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.ucd.shortlink.project.dao.entity.ShortLinkDO;
import org.ucd.shortlink.project.dao.mapper.ShortLinkMapper;
import org.ucd.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import org.ucd.shortlink.project.service.RecycleBinService;

import static org.ucd.shortlink.project.common.constant.RedisKeyConstant.REDIRECT_SHORT_LINK_KEY;

/**
 * Short link Recycle Bin interface implementor layer
 */
@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements RecycleBinService {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void saveRecycleBin(RecycleBinSaveReqDTO requestParam) {
        LambdaUpdateWrapper<ShortLinkDO> updateWrapper =
                Wrappers.lambdaUpdate(ShortLinkDO.class)
                        .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(ShortLinkDO::getGid, requestParam.getGid())
                        .eq(ShortLinkDO::getEnableStatus, 0)
                        .eq(ShortLinkDO::getDelFlag, 0);
        ShortLinkDO updateShortLink = ShortLinkDO.builder()
                .enableStatus(1)
                .build();
        baseMapper.update(updateShortLink, updateWrapper);

        // Remove Cache Value Here
        stringRedisTemplate.delete(String.format(REDIRECT_SHORT_LINK_KEY, requestParam.getFullShortUrl()));
    }
}
