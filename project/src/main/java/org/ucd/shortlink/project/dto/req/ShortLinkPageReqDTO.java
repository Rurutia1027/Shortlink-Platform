package org.ucd.shortlink.project.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.ucd.shortlink.project.dao.entity.ShortLinkDO;

/**
 * Short link page request params
 */

@Data
public class ShortLinkPageReqDTO extends Page<ShortLinkDO> {
    /**
     * Short link group ID
     */
    private String gid;

    /**
     * Order Tag
     */
    private String orderTag;
}
