package org.ucd.shortlink.admin.dto.resp;

import lombok.Data;

/**
 * Short link group response entity
 */
@Data
public class ShortLinkGroupRespDTO {
    /**
     * group id
     */
    private String gid;

    /**
     * group name
     */
    private String name;

    /**
     * username
     */
    private String username;

    /**
     * group sort order
     */
    private Integer sortOrder;
}
