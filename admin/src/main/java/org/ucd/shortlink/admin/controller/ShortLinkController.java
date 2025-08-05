package org.ucd.shortlink.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ucd.shortlink.admin.common.convention.result.Result;
import org.ucd.shortlink.admin.common.convention.result.Results;
import org.ucd.shortlink.admin.dto.resp.ShortLinkGroupCountQueryRespDTO;
import org.ucd.shortlink.admin.remote.ShortLinkRemoteService;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkUpdateReqDTO;
import org.ucd.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import org.ucd.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;

import java.util.List;

@RestController
public class ShortLinkController {

    // TODO: Refactor into SpringCloud Feign invocation
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };

    /**
     * Create short link
     */
    @PostMapping("/api/short-link/admin/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return shortLinkRemoteService.createShortLink(requestParam);
    }

    /**
     * Update Short Link
     */
    @PutMapping("/api/short-link/admin/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkRemoteService.updateShortLink(requestParam);
        return Results.success();
    }


    /**
     * Paging query short link
     */
    @PostMapping("/api/short-link/admin/v1/page")
    public Result<Page<ShortLinkPageRespDTO>> pageShortLink(@RequestBody ShortLinkPageReqDTO requestParam) {
        return shortLinkRemoteService.pageShortLink(requestParam);
    }

    /**
     * Query short link count per group
     */
    @GetMapping("/api/short-link/admin/v1/count")
    public Result<List<ShortLinkGroupCountQueryRespDTO>> listGroupShortLinkCount(@RequestParam List<String> requestParam) {
        return shortLinkRemoteService.listGroupShortLinkCount(requestParam);
    }
}
