package org.ucd.shortlink.admin.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Short link monitor region info response body
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsLocaleRespDTO {
    /**
     * Total cnt
     */
    private Integer cnt;

    /**
     * Region info
     */
    private String locale;

    /**
     * Ration of current region / total region
     */
    private Double ratio;
}
