package org.ucd.shortlink.project.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Short link device monitor stats response body
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsDeviceRespDTO {
    /**
     * Total cnt
     */
    private Integer cnt;

    /**
     * Device type
     */
    private String device;

    /**
     * Ration of current device / total device
     */
    private Double ratio;
}
