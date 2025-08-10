package org.ucd.shortlink.project.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Short link monitor metric record response bod
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsAccessRecordRespDTO {
    /**
     * Unique visitor type
     */
    private String uvType;

    /**
     * Browser
     */
    private String browser;

    /**
     * OS type
     */
    private String os;

    /**
     * IP
     */
    private String ip;

    /**
     * Device type
     */
    private String device;

    /**
     * Region
     */
    private String locale;

    /**
     * User info
     */
    private String user;

    /**
     * Visit timestamp
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
