package org.ucd.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ucd.shortlink.project.common.database.BaseDO;

import java.util.Date;

/**
 * Geography Location Monitor Metrics Entity
 */
@Data
@TableName("t_link_locale_stats")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkLocaleStatsDO extends BaseDO {
    /**
     * ID
     */
    private Long id;

    /**
     * Full short url address
     */
    private String fullShortUrl;

    /**
     * Group ID
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
     * Province name
     */
    private String province;

    /**
     * City name
     */
    private String city;

    /**
     * City code
     */
    private String adcode;

    /**
     * Nation name
     */
    private String country;
}


