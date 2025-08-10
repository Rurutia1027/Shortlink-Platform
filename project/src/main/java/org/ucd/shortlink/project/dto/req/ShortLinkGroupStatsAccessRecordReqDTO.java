package org.ucd.shortlink.project.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.ucd.shortlink.project.dao.entity.LinkAccessLogsDO;

/**
 * Grouped short link monitor metrics
 */
@Data
public class ShortLinkGroupStatsAccessRecordReqDTO extends Page<LinkAccessLogsDO> {
    /**
     * Group ID
     */
    private String gid;

    /**
     * Start Date
     */
    private String startDate;

    /**
     * End Date
     */
    private String endDate;
}
