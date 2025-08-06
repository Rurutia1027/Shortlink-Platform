package org.ucd.shortlink.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.ucd.shortlink.project.dao.entity.ShortLinkDO;
import org.ucd.shortlink.project.dto.req.RecycleBinSaveReqDTO;

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
}
