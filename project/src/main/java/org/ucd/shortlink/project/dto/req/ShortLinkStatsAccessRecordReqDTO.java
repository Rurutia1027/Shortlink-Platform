package org.ucd.shortlink.project.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.ucd.shortlink.project.dao.entity.LinkAccessLogsDO;

/**
 * Short link monitor record request param
 */
@Data
public class ShortLinkStatsAccessRecordReqDTO extends Page<LinkAccessLogsDO> {

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
