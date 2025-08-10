package org.ucd.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.ucd.shortlink.project.dto.req.ShortLinkStatsAccessRecordReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkStatsAccessRecordRespDTO;
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

    /**
     * Fetch short link specified date range monitor metric stats
     *
     * @param requestParam request short link monitor metric request param
     * @return short link monitor metric records
     */
    IPage<ShortLinkStatsAccessRecordRespDTO> shortLinkStatsAccessRecord(ShortLinkStatsAccessRecordReqDTO requestParam);
}
