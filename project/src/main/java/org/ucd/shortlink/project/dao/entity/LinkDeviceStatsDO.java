package org.ucd.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Short link access device stats
 */
@Data
@TableName("t_link_device_stats")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkDeviceStatsDO {
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
     * Date
     */
    private Date date;

    /**
     * Access cnt
     */
    private Integer cnt;

    /**
     * Device
     */
    private String device;
}
