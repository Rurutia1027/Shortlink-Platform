package org.ucd.shortlink.admin.dto.req;

import lombok.Data;

/**
 * Short link group sort request entity
 */
@Data
public class ShortLinkGroupSortRespDTO {

    /**
     * Group ID
     */
    private String gid;

    /**
     * Sort order
     */
    private Integer sortOrder;
}
