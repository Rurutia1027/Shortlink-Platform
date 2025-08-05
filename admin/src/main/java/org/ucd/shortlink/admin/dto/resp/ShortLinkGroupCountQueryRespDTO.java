package org.ucd.shortlink.admin.dto.resp;

import lombok.Data;

/**
 * Short link count query response body
 */
@Data
public class ShortLinkGroupCountQueryRespDTO {
    /**
     * short link group ID
     */
    private String gid;

    /**
     * short link count in current short link group
     */
    private Integer shortLinkCount;
}
