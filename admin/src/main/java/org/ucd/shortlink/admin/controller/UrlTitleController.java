package org.ucd.shortlink.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ucd.shortlink.admin.common.convention.result.Result;
import org.ucd.shortlink.admin.common.convention.result.Results;
import org.ucd.shortlink.admin.remote.ShortLinkRemoteService;

/**
 * URL Title Fetch Controller
 */

@RestController
@RequiredArgsConstructor
public class UrlTitleController {
    // TODO: Refactor into SpringCloud Feign invocation
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };

    /**
     * Fetch website title via provided url
     * @param url url address
     * @return website title of the given url
     */
    @GetMapping("/api/short-link/admin/v1/title")
    public Result<String> getTitleByUrl(@RequestParam("url") String url) {
        return shortLinkRemoteService.getTitleByUrl(url);
    }
}
