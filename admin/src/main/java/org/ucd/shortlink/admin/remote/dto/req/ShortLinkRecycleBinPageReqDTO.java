package org.ucd.shortlink.admin.remote.dto.req;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

/**
 * Paging query recycled short link request body
 */

@Data
public class ShortLinkRecycleBinPageReqDTO extends Page {

    /**
     * Short link group ID list
     */
    List<String> gidList;
}
