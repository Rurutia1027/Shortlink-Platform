package org.ucd.shortlink.project.common.database;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Database persistent layer base entity
 */
@Data
public class BaseDO {
    /**
     * Create time
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * Update time
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * Delete Flag: 0: non-delete, 1: deleted
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;
}
