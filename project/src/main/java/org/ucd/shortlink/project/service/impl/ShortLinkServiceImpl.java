package org.ucd.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ucd.shortlink.project.common.convention.exception.ClientException;
import org.ucd.shortlink.project.common.convention.exception.ServiceException;
import org.ucd.shortlink.project.common.enums.ValiDateTypeEnum;
import org.ucd.shortlink.project.dao.entity.ShortLinkDO;
import org.ucd.shortlink.project.dao.mapper.ShortLinkMapper;
import org.ucd.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkPageReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkGroupCountQueryRespDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import org.ucd.shortlink.project.service.ShortLinkService;
import org.ucd.shortlink.project.toolkit.HashUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Short link service implementor
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    private final RBloomFilter<String> shortUriCreationCachePenetrationBloomFilter;

    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam) {
        String shortLinkSuffix = generateSuffix(requestParam);
        String fullShortUrl = StrBuilder.create(requestParam.getDomain())
                .append("/").append(shortLinkSuffix).toString();
        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .domain(requestParam.getDomain())
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .createdType(requestParam.getCreatedType())
                .validDate(requestParam.getValidDate())
                .validDateType(requestParam.getValidDateType())
                .describe(requestParam.getDescribe())
                .shortUri(shortLinkSuffix)
                .fullShortUrl(fullShortUrl)
                .enableStatus(0)
                .build();

        try {
            baseMapper.insert(shortLinkDO);
        } catch (DuplicateKeyException ex) {
            // TODO: how to handle error detected short link url ?
            // - short link url -> already exist in cache
            // - short link url -> not exist in cache
            if (!isShortUriDBAvailable(fullShortUrl)) {
                log.warn("Short Link: {} duplicated insert to DB", fullShortUrl);
                throw new ServiceException("Short link URL duplicate generated!");
            }
        }

        shortUriCreationCachePenetrationBloomFilter.add(fullShortUrl);
        return ShortLinkCreateRespDTO.builder()
                .originalUrl(requestParam.getOriginUrl())
                .fullShortUrl(shortLinkDO.getFullShortUrl())
                .gid(requestParam.getGid())
                .build();
    }

    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam) {
        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getEnableStatus, 0)
                .eq(ShortLinkDO::getDelFlag, 0)
                .orderByDesc(ShortLinkDO::getCreateTime);

        IPage<ShortLinkDO> resultPage = baseMapper.selectPage(requestParam, queryWrapper);
        return resultPage.convert(each -> BeanUtil.toBean(each, ShortLinkPageRespDTO.class));
    }

    @Override
    public List<ShortLinkGroupCountQueryRespDTO> listGroupShortLinkCount(List<String> requestParam) {
        QueryWrapper<ShortLinkDO> queryWrapper = Wrappers.query(new ShortLinkDO())
                .select("gid as gid, count(*) as shortLinkCount")
                .in("gid", requestParam)
                .eq("enable_status", 0)
                .eq("del_flag", 0)
                .groupBy("gid");
        List<Map<String, Object>> shortLinkDOList = baseMapper.selectMaps(queryWrapper);
        return BeanUtil.copyToList(shortLinkDOList, ShortLinkGroupCountQueryRespDTO.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateShortLink(ShortLinkUpdateReqDTO requestParam) {
        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortLinkDO::getDelFlag, 0)
                .eq(ShortLinkDO::getEnableStatus, 0);

        ShortLinkDO hasShortLinkDO = baseMapper.selectOne(queryWrapper);

        if (hasShortLinkDO == null) {
            throw new ClientException("Short Link does not exist!");
        }

        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .domain(hasShortLinkDO.getDomain())
                .shortUri(hasShortLinkDO.getShortUri())
                .clickNum(hasShortLinkDO.getClickNum())
                .createdType(hasShortLinkDO.getCreatedType())
                .gid(requestParam.getGid())
                .originUrl(requestParam.getOriginUrl())
                .describe(requestParam.getDescribe())
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .build();

        if (Objects.equals(hasShortLinkDO.getGid(), requestParam.getGid())) {
            LambdaUpdateWrapper<ShortLinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                    .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                    .eq(ShortLinkDO::getGid, requestParam.getGid())
                    .eq(ShortLinkDO::getDelFlag, 0)
                    .eq(ShortLinkDO::getEnableStatus, 0)
                    .set(Objects.equals(requestParam.getValidDateType(),
                                    ValiDateTypeEnum.PERMANENT.getType()),
                            ShortLinkDO::getValidDate, null);

            baseMapper.update(shortLinkDO, updateWrapper);
        } else {
            LambdaUpdateWrapper<ShortLinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                    .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                    .eq(ShortLinkDO::getGid, hasShortLinkDO.getGid())
                    .eq(ShortLinkDO::getDelFlag, 0)
                    .eq(ShortLinkDO::getEnableStatus, 0);

            baseMapper.delete(updateWrapper);
            baseMapper.insert(shortLinkDO);
        }
    }

    private Boolean isShortUriCacheAvailable(String fullShortUrl) {
        return !shortUriCreationCachePenetrationBloomFilter.contains(fullShortUrl);
    }

    private Boolean isShortUriDBAvailable(String fullShortUri) {
        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers
                .lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getFullShortUrl, fullShortUri);
        ShortLinkDO shortLinkDO = baseMapper.selectOne(queryWrapper);
        return shortLinkDO == null;
    }

    private String generateSuffix(ShortLinkCreateReqDTO requestParam) {
        String originalUrl = requestParam.getOriginUrl();
        String shortUri = null;
        int customGenerateCount = 0;
        while (true) {
            if (customGenerateCount > 10) {
                throw new ServiceException("Generate random short link too frequent, please " +
                        "try later!");
            }
            originalUrl += System.currentTimeMillis();
            shortUri = HashUtil.hashToBase62(originalUrl);

            if (isShortUriCacheAvailable(requestParam.getDomain() + "/" + shortUri)) {
                break;
            }

            customGenerateCount++;
        }

        return shortUri;
    }
}
