package org.ucd.shortlink.admin.remote.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Short link creation request object
 */
@Data
public class ShortLinkCreateReqDTO {
    /**
     * Protocol name
     */
    private String domainProtocol;

    /**
     * Domain name
     */
    private String domain;

    /**
     * Original URL address
     */
    private String originUrl;

    /**
     * Short Link Group ID
     */
    private String gid;

    /**
     * Short Link create type: 0: interface request, 1: console command request
     */
    private Integer createdType;

    /**
     * Validate Date Type: 0: permanent, 1: customize
     */
    private Integer validDateType;

    /**
     * Short link validate date range
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validDate;


    /**
     * Description
     */
    private String describe;
}
