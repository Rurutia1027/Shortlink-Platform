package org.ucd.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ucd.shortlink.project.common.convention.exception.ClientException;
import org.ucd.shortlink.project.common.convention.exception.ServiceException;
import org.ucd.shortlink.project.common.enums.ValiDateTypeEnum;
import org.ucd.shortlink.project.dao.entity.ShortLinkDO;
import org.ucd.shortlink.project.dao.entity.ShortLinkRouteDO;
import org.ucd.shortlink.project.dao.mapper.ShortLinkMapper;
import org.ucd.shortlink.project.dao.mapper.ShortLinkRouteMapper;
import org.ucd.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkPageReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkGroupCountQueryRespDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import org.ucd.shortlink.project.service.ShortLinkService;
import org.ucd.shortlink.project.toolkit.HashUtil;
import org.ucd.shortlink.project.toolkit.LinkUtil;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.ucd.shortlink.project.common.constant.RedisKeyConstant.LOCK_REDIRECT_SHORT_LINK_KEY;
import static org.ucd.shortlink.project.common.constant.RedisKeyConstant.REDIRECT_IS_BLANK_SHORT_LINK_KEY;
import static org.ucd.shortlink.project.common.constant.RedisKeyConstant.REDIRECT_SHORT_LINK_KEY;

/**
 * Short link service implementor
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    private final RBloomFilter<String> shortUriCreationCachePenetrationBloomFilter;
    private final ShortLinkRouteMapper shortLinkRouteMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;

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
                .favicon(getFavicon(requestParam.getOriginUrl()))
                .shortUri(shortLinkSuffix)
                .fullShortUrl(fullShortUrl)
                .enableStatus(0)
                .build();

        ShortLinkRouteDO shortLinkRouteDO = ShortLinkRouteDO.builder()
                .fullShortUrl(fullShortUrl)
                .gid(requestParam.getGid())
                .build();

        try {
            baseMapper.insert(shortLinkDO);
            shortLinkRouteMapper.insert(shortLinkRouteDO);
        } catch (DuplicateKeyException ex) {
            // TODO: how to handle error detected short link url ?
            // - short link url -> already exist in cache
            // - short link url -> not exist in cache
            if (!isShortUriDBAvailable(fullShortUrl)) {
                log.warn("Short Link: {} duplicated insert to DB", fullShortUrl);
                throw new ServiceException("Short link URL duplicate generated!");
            }
        }

        // Cache Warm Up Here
        stringRedisTemplate.opsForValue().set(
                String.format(REDIRECT_SHORT_LINK_KEY, fullShortUrl),
                requestParam.getOriginUrl(),
                LinkUtil.genShortLinkCacheValidDate(requestParam.getValidDate()), TimeUnit.MILLISECONDS
        );
        shortUriCreationCachePenetrationBloomFilter.add(fullShortUrl);
        return ShortLinkCreateRespDTO.builder()
                .originalUrl(requestParam.getOriginUrl())
                .fullShortUrl("http://" + shortLinkDO.getFullShortUrl())
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
        return resultPage.convert(each -> {
            ShortLinkPageRespDTO result = BeanUtil.toBean(each, ShortLinkPageRespDTO.class);
            result.setDomain("http://" + result.getDomain());
            return result;
        });
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

    @SneakyThrows
    @Override
    public void restoreUrl(String shortUri, HttpServletRequest request, HttpServletResponse response) {
        String fullShortUrl = request.getServerName() + "/" + shortUri;
        String originalLink = stringRedisTemplate.opsForValue().get(String.format(REDIRECT_SHORT_LINK_KEY, fullShortUrl));

        // first cache query fetch original url address, directly redirect and exit
        if (StrUtil.isNotBlank(originalLink)) {
            response.sendRedirect(originalLink);
            return;
        }

        boolean containsShortUri =
                shortUriCreationCachePenetrationBloomFilter.contains(fullShortUrl);

        // bloom filter detected queried short link url do not have cached redirect url
        // address, directly return
        if (!containsShortUri) {
            // Risk Control:
            response.sendRedirect("/page/notfound");
            return;
        }

        String redirectBlankShortLinkUrl = stringRedisTemplate.opsForValue()
                .get(String.format(REDIRECT_IS_BLANK_SHORT_LINK_KEY, fullShortUrl));

        // queried short url mapped a blank/invalid redirect url address, redirect invalid,
        // directly return
        if (StrUtil.isNotBlank(redirectBlankShortLinkUrl)) {
            // Risk Control:
            response.sendRedirect("/page/notfound");
            return;
        }

        // first cache cannot fetch original url address, race for lock and then query DB
        RLock lock = redissonClient.getLock(String.format(LOCK_REDIRECT_SHORT_LINK_KEY,
                fullShortUrl));
        lock.lock();
        try {
            // when lock fetched via current thread, re-try fetch original url address from
            // redis cache; why second-fetch because this lock can be release by previous db
            // query thread -- after db fetching original url address already updated
            // to redis cache
            originalLink = stringRedisTemplate.opsForValue().get(String.format(REDIRECT_SHORT_LINK_KEY, fullShortUrl));

            // fetched original url address success redirect directly
            if (StrUtil.isNotBlank(originalLink)) {
                // Risk Control:
                response.sendRedirect(originalLink);
                return;
            }

            // redis cache cannot find cached original url value, then query db
            LambdaQueryWrapper<ShortLinkRouteDO> linkRouteQueryWrapper = Wrappers
                    .lambdaQuery(ShortLinkRouteDO.class)
                    .eq(ShortLinkRouteDO::getFullShortUrl, fullShortUrl);

            // query short-link:gid routing table to fetch which group/gid this short-link
            // locates in
            ShortLinkRouteDO shortLinkRouteDO = shortLinkRouteMapper
                    .selectOne(linkRouteQueryWrapper);

            if (shortLinkRouteDO == null) {
                // Risk Control: if we got here, it means there are some
                // fraud requests try to attach this entry point by using invalid short url
                // just cache a dash as occupation value
                stringRedisTemplate.opsForValue().set(
                        String.format(REDIRECT_IS_BLANK_SHORT_LINK_KEY, fullShortUrl),
                        "-",
                        30,
                        TimeUnit.MINUTES
                );
                response.sendRedirect("/page/notfound");
                return;
            }

            // then take gid continue query short link table to fetch original url
            LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                    .eq(ShortLinkDO::getGid, shortLinkRouteDO.getGid())
                    .eq(ShortLinkDO::getFullShortUrl, fullShortUrl)
                    .eq(ShortLinkDO::getDelFlag, 0)
                    .eq(ShortLinkDO::getEnableStatus, 0);
            ShortLinkDO shortLinkDO = baseMapper.selectOne(queryWrapper);

            // short link's original url address fetched, first save to Cache(so that those
            // the same redis lock fetchers do not need to query DB anymore -- second query
            // redis cache the original url address can be grabbed), after updating cache then
            // continue executing redirection
            if (shortLinkDO == null || (shortLinkDO.getValidDate() != null && shortLinkDO.getValidDate().before(new Date()))) {
                // if queried short link item already got expired, add a dash to cache occupy

                stringRedisTemplate.opsForValue().set(
                            String.format(REDIRECT_IS_BLANK_SHORT_LINK_KEY, fullShortUrl),
                            "-",
                            30,
                            TimeUnit.MINUTES
                    );
                    // an expired short link cannot be redirected, redirect to not found
                    // page, then return
                    response.sendRedirect("/page/notfound");
                    return;
                }

                stringRedisTemplate.opsForValue().set(String.format(REDIRECT_SHORT_LINK_KEY,
                        fullShortUrl), shortLinkDO.getOriginUrl());
                response.sendRedirect(shortLinkDO.getOriginUrl());
        } finally {
            lock.unlock();
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

    @SneakyThrows
    private String getFavicon(String url) {
        URL targetUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (HttpURLConnection.HTTP_OK == responseCode) {
            Document document = Jsoup.connect(url).get();
            Element faviconLink = document.select("link[rel~=(?i)^(shortcut )?icon]").first();
            if (faviconLink != null) {
                return faviconLink.attr("abs:href");
            }
        }
        return null;
    }
}
