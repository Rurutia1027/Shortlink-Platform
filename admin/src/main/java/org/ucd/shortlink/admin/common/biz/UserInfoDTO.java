package org.ucd.shortlink.admin.common.biz;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User info entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {
    /**
     * User ID
     */
    @JSONField(name = "id")
    private String userId;

    /**
     * Username
     */
    private String username;

    /**
     * User info name
     */
    private String realName;
}
