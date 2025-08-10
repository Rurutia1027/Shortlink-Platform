package org.ucd.shortlink.admin.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * Short link monitor record request param
 */
@Data
public class ShortLinkStatsAccessRecordReqDTO extends Page {

    /**
     * Full short link
     */
    private String fullShortUrl;

    /**
     * Group ID
     */
    private String gid;

    /**
     * Start date
     */
    private String startDate;

    /**
     * End date
     */
    private String endDate;
}
