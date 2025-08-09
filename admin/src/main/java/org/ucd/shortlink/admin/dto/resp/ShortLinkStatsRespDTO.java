package org.ucd.shortlink.admin.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Short link monitor metrics response body
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsRespDTO {
    /**
     * Page visitor cnt
     */
    private Integer pv;

    /**
     * Unique visitor cnt
     */
    private Integer uv;

    /**
     * Unique Ip access cnt
     */
    private Integer uip;

    /**
     * Daily basic access statistic metrics
     */
    private List<ShortLinkStatsAccessDailyRespDTO> daily;

    /**
     * Region access statistic metrics
     */
    private List<ShortLinkStatsLocaleRespDTO> localeStats;

    /**
     * Hourly access statistics metrics
     */
    private List<Integer> hourStats;

    /**
     * High frequency access IP info
     */
    private List<ShortLinkStatsTopIpRespDTO> topIpStats;

    /**
     * Weekly access statistics
     */
    private List<Integer> weekdayStats;

    /**
     * Browser access info metrics
     */
    private List<ShortLinkStatsBrowserRespDTO> browserStats;


    /**
     * OS access info metrics
     */
    private List<ShortLinkStatsOsRespDTO> osStats;


    /**
     * Visitor type info metric statistics
     */
    private List<ShortLinkStatsUvRespDTO> uvTypeStats;

    /**
     * Device info metric statistics
     */
    private List<ShortLinkStatsDeviceRespDTO> deviceStats;

    /**
     * Network info metric statistics
     */
    private List<ShortLinkStatsNetworkRespDTO> networkStats;
}
