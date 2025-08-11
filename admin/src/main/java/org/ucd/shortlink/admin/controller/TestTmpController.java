package org.ucd.shortlink.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ucd.shortlink.admin.common.convention.result.Result;
import org.ucd.shortlink.admin.common.convention.result.Results;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkStatsReqDTO;

@RestController
public class TestTmpController {
    @GetMapping("/api/short-link/admin/v1/tests")
    public Result<String> shortLinkStats(ShortLinkStatsReqDTO requestParam) {
        return Results.success("Test OK");
    }
}
