package org.ucd.shortlink.project.dto.req;

import lombok.Data;

/**
 * Recycle Bin Remove Request Parameters
 */

@Data
public class RecycleBinRemoveReqDTO {
    /**
     * Short Link Group ID
     */
    private String gid;

    /**
     * Short Link Full Short Url Address
     */
    private String fullShortUrl;
}
