package org.ucd.shortlink.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.ucd.shortlink.admin.common.convention.result.Result;
import org.ucd.shortlink.admin.common.convention.result.Results;
import org.ucd.shortlink.admin.remote.ShortLinkRemoteService;
import org.ucd.shortlink.admin.remote.dto.req.RecycleBinSaveReqDTO;

/**
 * Recycle Bin Controller layer
 */

@RestController
@RequiredArgsConstructor
public class RecycleBinController {
    // TODO: Refactor into SpringCloud Feign invocation
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };

    /**
     * Move short link to recycle bin
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO recycleBinSaveReqDTO) {
        shortLinkRemoteService.saveRecycleBin(recycleBinSaveReqDTO);
        return Results.success();
    }
}
