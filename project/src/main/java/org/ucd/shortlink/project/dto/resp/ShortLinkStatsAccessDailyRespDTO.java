package org.ucd.shortlink.project.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Short link daily metric statistics
 */
@Data
public class ShortLinkStatsAccessDailyRespDTO {
    /**
     * Date
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;

    /**
     * Page View Cnt
     */
    private Integer pv;

    /**
     * Unique User View
     */
    private Integer uv;

    /**
     * Unique IP Visit
     */
    private Integer uip;

}
