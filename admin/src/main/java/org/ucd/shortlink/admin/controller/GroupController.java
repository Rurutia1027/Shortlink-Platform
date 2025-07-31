package org.ucd.shortlink.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.ucd.shortlink.admin.common.convention.result.Result;
import org.ucd.shortlink.admin.common.convention.result.Results;
import org.ucd.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import org.ucd.shortlink.admin.service.GroupService;

/**
 * short linke grouping control layer
 */
@RestController
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping("/api/short-link/v1/group")
    Result<Void> save(@RequestBody ShortLinkGroupSaveReqDTO requestParam) {
        groupService.saveGroup(requestParam.getName());
        return Results.success();
    }
}
