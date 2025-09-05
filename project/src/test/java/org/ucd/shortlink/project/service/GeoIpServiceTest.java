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

package org.ucd.shortlink.project.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.ucd.shortlink.project.configs.GeoIPTestConfig;
import org.ucd.shortlink.project.dao.entity.GeoIpInfoDO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = GeoIPTestConfig.class)
public class GeoIpServiceTest {
    @Autowired
    GeoIpService service;

    @Test
    public void initOK() {
        assertNotNull(service);
        assertNotNull(service.getRestTemplate());
    }

    @Test
    public void testGeoIpService() {
        assertEquals("AU", service.resolve("1.1.1.1"));
        assertEquals("CN", service.resolve("114.114.114.114"));
        assertEquals("US", service.resolve("8.8.8.8"));
    }

    @Test
    public void testGeoIpFullInfo() {
        GeoIpInfoDO info = service.resolveFull("1.1.1.1");
        assertNotNull(info);
        assertEquals("AU", info.getCountryCode());
        assertEquals("Australia", info.getCountry());

        GeoIpInfoDO cnInfo = service.resolveFull("114.114.114.114");
        assertEquals("CN", cnInfo.getCountryCode());
        assertEquals("China", cnInfo.getCountry());

        GeoIpInfoDO unknown = service.resolveFull("255.255.255.255");
        assertEquals(null, unknown); // or handle as UNKNOWN
    }
    @Test
    public void testGeoIp_CN() {
        GeoIpInfoDO cnInfo = service.resolveFull("114.114.114.114");
        assertNotNull(cnInfo);
        assertEquals("CN", cnInfo.getCountryCode());
        assertEquals("China", cnInfo.getCountry());
        assertNotNull(cnInfo.getProvince());
        assertNotNull(cnInfo.getCity());
        assertNotNull(cnInfo.getAdcode());
    }

    @Test
    public void testGeoIp_US() {
        GeoIpInfoDO usInfo = service.resolveFull("8.8.8.8");
        assertNotNull(usInfo);
        assertEquals("US", usInfo.getCountryCode());
        assertEquals("United States", usInfo.getCountry());
        assertNotNull(usInfo.getProvince());
        assertNotNull(usInfo.getCity());
    }

    @Test
    public void testGeoIp_AU() {
        GeoIpInfoDO auInfo = service.resolveFull("1.1.1.1");
        assertNotNull(auInfo);
        assertEquals("AU", auInfo.getCountryCode());
        assertEquals("Australia", auInfo.getCountry());
        assertNotNull(auInfo.getProvince());
        assertNotNull(auInfo.getCity());
    }

    @Test
    public void testGeoIp_InvalidIP() {
        GeoIpInfoDO unknown = service.resolveFull("255.255.255.255");
        assertNull(unknown); // Expect null or your "UNKNOWN" handling
    }
}
