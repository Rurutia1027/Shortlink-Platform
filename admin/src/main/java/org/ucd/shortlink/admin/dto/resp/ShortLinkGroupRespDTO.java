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
     * group sort order
     */
    private Integer sortOrder;

    /**
     * short link cnt in current group
     */
    private Integer shortLinkCount;
}
