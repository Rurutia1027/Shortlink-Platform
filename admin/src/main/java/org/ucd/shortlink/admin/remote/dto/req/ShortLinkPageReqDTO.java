package org.ucd.shortlink.admin.remote.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * Short link page request params
 */

@Data
public class ShortLinkPageReqDTO extends Page {
    /**
     * Short link group ID
     */
    private String gid;

}
