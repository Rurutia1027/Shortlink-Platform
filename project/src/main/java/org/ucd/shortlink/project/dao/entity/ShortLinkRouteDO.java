package org.ucd.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Short Link Redirect Route Entity
 */
@Data
@Builder
@TableName("t_link_route")
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkRouteDO {
    /**
     * Entity ID
     */
    private Long id;

    /**
     * Group ID
     */
    private String gid;

    /**
     * Short Link full short url address
     */
    private String fullShortUrl;
}
