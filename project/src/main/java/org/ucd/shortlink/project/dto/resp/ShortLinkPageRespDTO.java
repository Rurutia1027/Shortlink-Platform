package org.ucd.shortlink.project.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Short link page response body
 */

@Data
public class ShortLinkPageRespDTO {

    private Long id;

    private String domain;

    private String shortUri;

    private String fullShortUrl;

    private String originUrl;

    private String gid;

    private Integer validateType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validDate;

    private String describe;

    private String favicon;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * History pv cnt
     */
    private Integer totalPv;

    /**
     * Today pv cnt
     */
    private Integer toDayPv;

    /**
     * History uv cnt
     */
    private Integer totalUv;

    /**
     * Today uv cnt
     */
    private Integer toDayUv;

    /**
     * History total uip
     */
    private Integer totalUIp;

    /**
     * Today total uip
     */
    private Integer toDayUIp;
}
