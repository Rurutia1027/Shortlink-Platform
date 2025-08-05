package org.ucd.shortlink.project.dto.resp;

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
