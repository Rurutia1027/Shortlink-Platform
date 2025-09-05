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

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.ucd.shortlink.project.dao.entity.GeoIpInfoDO;

import java.util.Map;

@Data
@Slf4j
@Service
public class GeoIpService {
    private static final String GEO_IP_API_URL = "http://ip-api.com/json/";
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Resolve IP to country code (CN, US, etc.)
     *
     * @param ip IPv4 or IPv6
     * @return country code or "UNKNOWN"
     */
    public String resolve(String ip) {
        try {
            Map<String, Object> response = restTemplate.getForObject(GEO_IP_API_URL + ip, Map.class);
            if (response != null && "success".equals(response.get("status"))) {
                return response.get("countryCode") != null ? response.get("countryCode").toString() : "UNKNOWN";
            }
        } catch (Exception e) {
            log.warn("Failed to resolve IP {}: {}", ip, e.getMessage());
        }
        return "UNKNOWN";
    }

    /**
     * Resolve IP to full geo info
     */
    public GeoIpInfoDO resolveFull(String ip) {
        try {
            String url = GEO_IP_API_URL + ip;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && "success".equals(response.get("status"))) {
                return GeoIpInfoDO.builder()
                        .country((String) response.get("country"))
                        .countryCode((String) response.get("countryCode"))
                        .province((String) response.get("regionName"))
                        .city((String) response.get("city"))
                        .adcode(response.get("zip") != null ? response.get("zip").toString() : null)
                        .build();
            }
        } catch (Exception e) {
            log.warn("Failed to resolve IP {}: {}", ip, e.getMessage());
        }
        return null;
    }
}
