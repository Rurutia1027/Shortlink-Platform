package org.ucd.shortlink.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.ucd.shortlink.admin.common.convention.result.Result;
import org.ucd.shortlink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.ucd.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;

/**
 * Short Link recycle interface layer
 */
public interface RecycleBinService {
    Result<IPage<ShortLinkPageRespDTO>> pageRecycledShortLink(ShortLinkRecycleBinPageReqDTO requestParam);
}
