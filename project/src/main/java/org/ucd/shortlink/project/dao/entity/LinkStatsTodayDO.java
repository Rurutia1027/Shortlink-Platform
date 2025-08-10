package org.ucd.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("t_link_stats_today")
@Data
public class LinkStatsTodayDO {
    /**
     * ID
     */
    private Long id;

    /**
     * Group ID
     */
    private String gid;

    /**
     * Full short url
     */
    private String fullShortUrl;

    /**
     * Date
     */
    private Date date;

    /**
     * Today PV
     */
    private Integer todayPv;

    /**
     * Today UV
     */
    private Integer todayUv;
}
