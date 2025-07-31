package org.ucd.shortlink.admin.dto.req;

import lombok.Data;

/**
 * short link group update parameter
 */
@Data
public class ShortLinkGroupUpdateReqDTO {
    /**
     * group id
     */
    private String gid;

    /**
     * group name
     */
    private String name;
}
