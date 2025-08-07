package org.ucd.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ucd.shortlink.project.common.database.BaseDO;

import java.util.Date;

/**
 * Short link monitor statistic entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_link_access_stats")
public class LinkAccessStatsDO extends BaseDO {

    /**
     * id
     */
    private Long id;

    /**
     * full short link url address
     */
    private String fullShortUrl;

    /**
     * Date
     */
    private Date date;

    /**
     * PV
     */
    private Integer pv;

    /**
     * UV
     */
    private Integer uv;

    /**
     * IP CNT
     */
    private Integer uip;

    /**
     * Group ID
     */
    private String gid;

    /**
     * Hour
     */
    private Integer hour;

    /**
     * Week
     */
    private Integer weekday;
}
