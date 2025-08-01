package org.ucd.shortlink.project.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Short link creation response body
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkCreateRespDTO {

    /**
     * Short link group ID
     */
    private String gid;

    /**
     * Short link original URL address
     */
    private String originalUrl;

    /**
     * Short link full short URL address
     */
    private String fullShortUrl;
}
