package org.ucd.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ucd.shortlink.project.common.database.BaseDO;

import java.util.Date;

/**
 * Short link OS info DO
 */
@Data
@TableName("t_link_os_stats")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkOsStatsDO extends BaseDO {
    /**
     * id
     */
    private Long id;

    /**
     * full short url
     */
    private String fullShortUrl;

    /**
     * group ID
     */
    private String gid;

    /**
     * date
     */
    private Date date;

    /**
     * access cnt
     */
    private Integer cnt;

    /**
     * OS
     */
    private String os;
}
