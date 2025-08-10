package org.ucd.shortlink.admin.remote.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * High frequency access IP monitor state response body
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsTopIpRespDTO {
    /**
     * Total cnt
     */
    private Integer cnt;

    /**
     * IP
     */
    private String ip;
}
