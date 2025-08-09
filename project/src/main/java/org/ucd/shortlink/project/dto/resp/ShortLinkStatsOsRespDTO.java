package org.ucd.shortlink.project.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Short link OS metrics response body
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsOsRespDTO {
    /**
     * Total cnt
     */
    private Integer cnt;

    /**
     * OS info
     */
    private String os;

    /**
     * Ratio of current os / total os
     */
    private Double ratio;
}
