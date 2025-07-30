package org.ucd.shortlink.admin.dto.req;

import lombok.Data;

/**
 * User registration request params
 */
@Data
public class UserRegisterReqDTO {
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String email;
}
