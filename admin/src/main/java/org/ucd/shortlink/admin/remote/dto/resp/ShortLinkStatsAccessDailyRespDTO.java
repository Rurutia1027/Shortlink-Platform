package org.ucd.shortlink.admin.remote.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Short link daily metric statistics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsAccessDailyRespDTO {
    /**
     * Date
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String date;

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
