/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ucd.shortlink.admin.common.enums;

import org.ucd.shortlink.admin.common.convention.errorcode.IErrorCode;

/**
 * User error code enumeration.
 */
public enum UserErrorCodeEnum implements IErrorCode {
    USER_TOKEN_FAIL("A000200", "User token validation failure"),
    USER_NULL("B000200", "User record does not exist"),
    USER_NAME_EXIST("B000201", "User name already exist"),
    USER_EXIST("B000202", "User record already exists"),
    USER_SAVE_ERROR("B000203", "User save failure"),
    USER_LOGIN_ERROR("B000204", "User login failure");

    private final String code;

    private final String message;

    UserErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
