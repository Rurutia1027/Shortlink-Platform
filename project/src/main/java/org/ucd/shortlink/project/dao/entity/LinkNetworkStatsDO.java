package org.ucd.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ucd.shortlink.project.common.database.BaseDO;

import java.util.Date;

/**
 * Short link network stats entity
 */
@Data
@TableName("t_link_network_stats")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkNetworkStatsDO extends BaseDO {

    /**
     * id
     */
    private Long id;

    /**
     * Full short url
     */
    private String fullShortUrl;

    /**
     * group ID
     */
    private String gid;

    /**
     * Date
     */
    private Date date;

    /**
     * Access cnt
     */
    private Integer cnt;

    /**
     * Access network
     */
    private String network;
}
