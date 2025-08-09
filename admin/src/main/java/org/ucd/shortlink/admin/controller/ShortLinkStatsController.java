package org.ucd.shortlink.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ucd.shortlink.admin.common.convention.result.Result;
import org.ucd.shortlink.admin.dto.req.ShortLinkStatsReqDTO;
import org.ucd.shortlink.admin.dto.resp.ShortLinkStatsRespDTO;
import org.ucd.shortlink.admin.remote.ShortLinkRemoteService;

/**
 * Short Link Admin side Monitor State Controller
 */

@RestController
@RequiredArgsConstructor
public class ShortLinkStatsController {

    /**
     * TODO: modify this to SpringCloud Feign
     */
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };

    /**
     * Short link monitor metric stats
     */
    @GetMapping("/api/short-link/admin/v1/stats")
    public Result<ShortLinkStatsRespDTO> shortLinkStats(ShortLinkStatsReqDTO requestParam) {
        return shortLinkRemoteService.oneShortLinkStats(requestParam);
    }


}
