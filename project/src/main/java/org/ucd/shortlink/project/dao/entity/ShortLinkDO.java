package org.ucd.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ucd.shortlink.project.common.database.BaseDO;

import java.util.Date;

/**
 * Short link entity
 */

@Data
@Builder
@TableName("t_link")
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkDO extends BaseDO {
    /**
     * Short link ID
     */
    private Long id;

    /**
     * Domain name
     */
    private String domain;

    /**
     * Short URI
     */
    private String shortUri;

    /**
     * Full Short URL
     */
    private String fullShortUrl;

    /**
     * Original URL address
     */
    private String originUrl;

    /**
     * Short Link click statistic number
     */
    private Integer clickNum;

    /**
     * Short Link Group ID
     */
    private String gid;

    /**
     * Enable Status: 0: enabled, 1: non-enabled
     */
    private Integer enableStatus;

    /**
     * Short Link create type: 0: interface request, 1: console command request
     */
    private Integer createdType;

    /**
     * Validate Date Type: 0: permanent, 1: customize
     */
    private Integer validDateType;

    /**
     * Short link validate date range
     */
    private Date validDate;


    /**
     * Description
     */
    @TableField("`describe`")
    private String describe;
}
