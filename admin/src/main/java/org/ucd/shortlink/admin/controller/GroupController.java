package org.ucd.shortlink.admin.controller;

import org.springframework.web.bind.annotation.RestController;
import org.ucd.shortlink.admin.service.GroupService;

/**
 * short linke grouping control layer
 */
@RestController
public class GroupController {
    private final GroupService groupService;
}
