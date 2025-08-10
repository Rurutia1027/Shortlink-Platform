package org.ucd.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.repository.query.Param;
import org.ucd.shortlink.project.dao.entity.ShortLinkDO;

/**
 * Short link persistence layer
 */
public interface ShortLinkMapper extends BaseMapper<ShortLinkDO> {
    /**
     * Short link access increment stats
     */
    @Update("update t_link set total_pv = total_pv + #{totalPv}, total_uv = total_uv + #{totalUv}, total_uip = total_uip + #{totalUip} where gid = #{gid} and full_short_url = #{fullShortUrl}")
    void incrementStats(
            @Param("gid") String gid,
            @Param("fullShortUrl") String fullShortUrl,
            @Param("totalPv") Integer totalPv,
            @Param("totalUv") Integer totalUv,
            @Param("totalUip") Integer totalUip
    );
}
