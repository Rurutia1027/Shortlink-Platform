package org.ucd.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.ucd.shortlink.project.dao.entity.ShortLinkDO;
import org.ucd.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkPageReqDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkPageRespDTO;

/**
 * Short link service layer
 */
public interface ShortLinkService extends IService<ShortLinkDO> {
    /**
     * Create short link
     *
     * @param requestParam create short link object params
     * @return short link creation response info
     */
    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam);

    /**
     * Page query short link items
     */
    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam);
}
