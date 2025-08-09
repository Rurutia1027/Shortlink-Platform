package org.ucd.shortlink.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ucd.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkStatsRespDTO;
import org.ucd.shortlink.project.service.ShortlinkStatsService;

/**
 * Short link monitor interface implementor
 */
@Service
@RequiredArgsConstructor
public class ShortlinkStatsServiceImpl implements ShortlinkStatsService {
    @Override
    public ShortLinkStatsRespDTO oneShortLinkStats(ShortLinkStatsReqDTO requestParam) {
        return null;
    }
}
