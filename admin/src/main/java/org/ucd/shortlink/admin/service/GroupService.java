package org.ucd.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.ucd.shortlink.admin.dao.entity.GroupDO;

/**
 * short link grouping service
 */
public interface GroupService extends IService<GroupDO> {
    /**
     * Add a short link grouping entity
     *
     * @param groupName short link group name
     */
    void saveGroup(String groupName);
}
