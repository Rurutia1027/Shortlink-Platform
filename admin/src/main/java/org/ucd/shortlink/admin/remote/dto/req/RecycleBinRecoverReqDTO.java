package org.ucd.shortlink.admin.remote.dto.req;

import lombok.Data;

/**
 * Recycle Bin Recover Request Parameters
 */

@Data
public class RecycleBinRecoverReqDTO {
    /**
     * Short Link Group ID
     */
    private String gid;

    /**
     * Short Link Full Short Url Address
     */
    private String fullShortUrl;
}
