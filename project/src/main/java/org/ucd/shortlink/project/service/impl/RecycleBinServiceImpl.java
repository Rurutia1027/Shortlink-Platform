package org.ucd.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.ucd.shortlink.project.dao.entity.ShortLinkDO;
import org.ucd.shortlink.project.dao.mapper.ShortLinkMapper;
import org.ucd.shortlink.project.dto.req.RecycleBinRecoverReqDTO;
import org.ucd.shortlink.project.dto.req.RecycleBinRemoveReqDTO;
import org.ucd.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import org.ucd.shortlink.project.service.RecycleBinService;

import static org.ucd.shortlink.project.common.constant.RedisKeyConstant.REDIRECT_IS_BLANK_SHORT_LINK_KEY;
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

    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .in(ShortLinkDO::getGid, requestParam.getGidList())
                .eq(ShortLinkDO::getEnableStatus, 1)
                .eq(ShortLinkDO::getDelFlag, 0)
                .orderByDesc(ShortLinkDO::getCreateTime);

        IPage<ShortLinkDO> resultPage = baseMapper.selectPage(requestParam, queryWrapper);
        return resultPage.convert(each -> {
            ShortLinkPageRespDTO result = BeanUtil.toBean(each, ShortLinkPageRespDTO.class);
            result.setDomain("http://" + result.getDomain());
            return result;
        });
    }

    @Override
    public void recoverRecycleBin(RecycleBinRecoverReqDTO requestParam) {
        LambdaUpdateWrapper<ShortLinkDO> updateWrapper =
                Wrappers.lambdaUpdate(ShortLinkDO.class)
                        .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(ShortLinkDO::getGid, requestParam.getGid())
                        .eq(ShortLinkDO::getEnableStatus, 1)
                        .eq(ShortLinkDO::getDelFlag, 0);
        ShortLinkDO updateShortLink = ShortLinkDO.builder()
                .enableStatus(0)
                .build();
        baseMapper.update(updateShortLink, updateWrapper);

        // Remove original Blank dash occupation out from the Cache
        stringRedisTemplate.delete(String.format(REDIRECT_IS_BLANK_SHORT_LINK_KEY,
                requestParam.getFullShortUrl()));
    }

    @Override
    public void removeRecycleBin(RecycleBinRemoveReqDTO requestParam) {
        LambdaUpdateWrapper<ShortLinkDO> updateWrapper =
                Wrappers.lambdaUpdate(ShortLinkDO.class)
                        .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(ShortLinkDO::getGid, requestParam.getGid())
                        .eq(ShortLinkDO::getEnableStatus, 1)
                        .eq(ShortLinkDO::getDelFlag, 0);
        baseMapper.delete(updateWrapper);
    }
}
