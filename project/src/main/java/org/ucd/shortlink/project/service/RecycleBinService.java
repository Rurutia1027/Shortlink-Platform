package org.ucd.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.ucd.shortlink.project.dao.entity.ShortLinkDO;
import org.ucd.shortlink.project.dto.req.RecycleBinRecoverReqDTO;
import org.ucd.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import org.ucd.shortlink.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.ucd.shortlink.project.dto.resp.ShortLinkPageRespDTO;

/**
 * Short Link Recycle Bin Service interface layer
 */
public interface RecycleBinService extends IService<ShortLinkDO> {
    /**
     * Save recycled Short Link Entity
     *
     * @param requestParam short link info to be recycled
     */
    void saveRecycleBin(RecycleBinSaveReqDTO requestParam);


    /**
     * Page query recycled short link items
     *
     * @param requestParam request parameters
     * @return return paging short link response
     */
    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam);

    void recoverRecycleBin(RecycleBinRecoverReqDTO requestParam);
}
