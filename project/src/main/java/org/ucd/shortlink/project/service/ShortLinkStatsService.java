package org.ucd.shortlink.project.service;

import org.ucd.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkStatsRespDTO;

/**
 * Short link monitor interface layer
 */
public interface ShortLinkStatsService {
    /**
     * Fetch single short link monitor metrics
     *
     * @param requestParam Single Short link monitor metrics query request
     * @return short link monitor metric response b
     */

    ShortLinkStatsRespDTO oneShortLinkStats(ShortLinkStatsReqDTO requestParam);
}
