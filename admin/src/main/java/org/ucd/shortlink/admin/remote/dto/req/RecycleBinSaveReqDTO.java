package org.ucd.shortlink.admin.remote.dto.req;

import lombok.Data;

/**
 * Recycle Bin Save Request Parameters
 */

@Data
public class RecycleBinSaveReqDTO {
    /**
     * Short Link Group ID
     */
    private String gid;

    /**
     * Short Link Full Short Url Address
     */
    private String fullShortUrl;
}
