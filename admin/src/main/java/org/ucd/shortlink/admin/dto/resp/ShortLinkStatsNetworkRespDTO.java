package org.ucd.shortlink.admin.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Short link access network metric stats response bod
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsNetworkRespDTO {
    /**
     * Total cnt
     */
    private Integer cnt;

    /**
     * Network info
     */
    private String network;

    /**
     * Ration of current network cnt / total network cnt
     */
    private Double ratio;
}
