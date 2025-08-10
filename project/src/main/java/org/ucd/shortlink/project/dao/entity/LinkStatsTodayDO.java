package org.ucd.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ucd.shortlink.project.common.database.BaseDO;

import java.util.Date;

@TableName("t_link_stats_today")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkStatsTodayDO extends BaseDO {
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

    /**
     * Today Unique IP Cnt
     */
    private Integer todayUip;
}
