package org.ucd.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ucd.shortlink.admin.common.database.BaseDO;

/**
 * Short link grouping entity
 */
@Data
@TableName("t_group")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDO extends BaseDO {
    /**
     * id
     */
    private Long id;

    /**
     * group id
     */
    private String gid;

    /***/
    private String name;

    private String username;
}
