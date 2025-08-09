package org.ucd.shortlink.project.dto.req;

import lombok.Data;

/**
 * Short link monitor metrics object
 */
@Data
public class ShortLinkStatsReqDTO {
    /**
     * Full short link
     */
    private String fullShortLink;

    /**
     * Group ID
     */
    private String gid;

    /**
     * Monitor metrics Query Start Date
     */
    private String startDate;

    /**
     * Monitor metrics Query End Date
     */
    private String endDate;
}
