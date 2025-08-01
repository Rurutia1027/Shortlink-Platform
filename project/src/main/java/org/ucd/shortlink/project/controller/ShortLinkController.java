package org.ucd.shortlink.project.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Short link controller layer
 */

@RestController
public class ShortLinkController {
    @PostMapping("/api/short-link/v1/short-link")
    public Result<Void>
}
