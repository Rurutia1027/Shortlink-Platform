package org.ucd.shortlink.admin.remote.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Short link monitor response body
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsUvRespDTO {
    /**
     * Total cnt
     */
    private Integer cnt;

    /**
     * Unique visitor type
     */
    private String uvType;

    /**
     * Ratio of current uv type / total uv type
     */
    private Double ratio;
}
