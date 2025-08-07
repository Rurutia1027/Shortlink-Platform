package org.ucd.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ucd.shortlink.project.common.database.BaseDO;

/**
 * Access log info entity
 */
@Data
@TableName("t_link_access_logs")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkAccessLogsDO extends BaseDO {

    /**
     * id
     */
    private Long id;

    /**
     * Full short url
     */
    private String fullShortUrl;

    /**
     * Group ID
     */
    private String gid;

    /**
     * User info
     */
    private String user;

    /**
     * Browser
     */
    private String browser;

    /**
     * Device info
     */
    private String device;

    /**
     * ip
     */
    private String ip;
}
