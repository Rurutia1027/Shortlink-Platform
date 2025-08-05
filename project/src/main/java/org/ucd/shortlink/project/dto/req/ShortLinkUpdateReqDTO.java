package org.ucd.shortlink.project.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Short link update request object
 */
@Data
public class ShortLinkUpdateReqDTO {
    /**
     * Original URL address
     */
    private String originUrl;


    /**
     * Full short url
     */
    private String fullShortUrl;

    /**
     * Short Link Group ID
     */
    private String gid;


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
