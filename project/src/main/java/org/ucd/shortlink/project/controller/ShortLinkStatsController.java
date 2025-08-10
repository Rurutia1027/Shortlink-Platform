package org.ucd.shortlink.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ucd.shortlink.project.common.convention.result.Result;
import org.ucd.shortlink.project.common.convention.result.Results;
import org.ucd.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkStatsRespDTO;
import org.ucd.shortlink.project.service.ShortLinkStatsService;

/**
 * Short link monitor metrics controller
 */
@RestController
@RequiredArgsConstructor
public class ShortLinkStatsController {
    private final ShortLinkStatsService shortlinkStatsService;

    /**
     * Short link item monitor metrics
     */
    @GetMapping("/api/short-link/v1/stats")
    public Result<ShortLinkStatsRespDTO> shortLinkStats(ShortLinkStatsReqDTO requestParams) {
        return Results.success(shortlinkStatsService.oneShortLinkStats(requestParams));
    }
}
