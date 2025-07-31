package org.ucd.shortlink.admin.dto.req;

import lombok.Data;

/**
 * Short link group request params
 */
@Data
public class ShortLinkGroupSaveReqDTO {
    /**
     * short link group name
     */
    private String name;
}
