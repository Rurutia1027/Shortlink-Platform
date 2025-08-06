package org.ucd.shortlink.project.dto.req;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.ucd.shortlink.project.dao.entity.ShortLinkDO;

import java.util.List;

/**
 * Paging query recycled short link request body
 */

@Data
public class ShortLinkRecycleBinPageReqDTO extends Page<ShortLinkDO> {

    /**
     * Short link group ID list
     */
    List<String> gidList;
}
