package org.ucd.shortlink.project.dto.resp;

import lombok.Data;

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

    private Integer validDate;

    private String describe;

    private String favicon;

}
