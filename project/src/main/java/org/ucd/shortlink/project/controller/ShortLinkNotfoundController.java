package org.ucd.shortlink.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Short link does not exist, redirect handle controller
 */
@Controller
public class ShortLinkNotfoundController {

    /**
     * Short link not exist redirect
     */
    @RequestMapping("/page/notfound")
    public String notfound() {
        return "notfound";
    }
}
