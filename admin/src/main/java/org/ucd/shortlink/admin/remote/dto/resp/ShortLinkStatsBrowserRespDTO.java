package org.ucd.shortlink.admin.remote.dto.resp;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Short link browser monitor metrics body
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsBrowserRespDTO {
    /**
     * Total cnt
     */
    private Integer cnt;

    /**
     * Browser info
     */
    private String browser;

    /**
     * Ratio of current browser cnt / total browser cnt
     */
    private Double ratio;
}
