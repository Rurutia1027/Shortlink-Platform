package org.ucd.shortlink.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ucd.shortlink.project.common.convention.result.Result;
import org.ucd.shortlink.project.common.convention.result.Results;
import org.ucd.shortlink.project.service.UrlTitleService;

/**
 * URL Title Fetch Controller
 */

@RestController
@RequiredArgsConstructor
public class UrlTitleController {
    private final UrlTitleService urlTitleService;

    /**
     * Fetch website title via provided url
     * @param url url address
     * @return website title of the given url
     */
    @GetMapping("/api/short-link/v1/title")
    public Result<String> getTitleByUrl(@RequestParam("url") String url) {
        String titleStr = urlTitleService.getTitleByUrl(url);
        return Results.success(titleStr);
    }
}
