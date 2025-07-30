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

package org.ucd.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.ucd.shortlink.admin.dao.entity.UserDO;
import org.ucd.shortlink.admin.dto.resp.UserRespDTO;

/**
 * User service interface
 */
public interface UserService extends IService<UserDO> {
    /**
     * Response user info by username
     * @param username user name
     * @return user response body
     */
    UserRespDTO getUserByUsername(String username);

    Boolean hasUserName(String username);
}
