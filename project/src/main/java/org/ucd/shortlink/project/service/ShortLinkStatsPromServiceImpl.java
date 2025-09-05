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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ucd.shortlink.project.dto.req.metrics.ShortLinkGroupStatsAccessRecordReqDTO;
import org.ucd.shortlink.project.dto.req.metrics.ShortLinkStatsAccessRecordReqDTO;
import org.ucd.shortlink.project.dto.req.metrics.ShortLinkStatsReqDTO;
import org.ucd.shortlink.project.dto.resp.metrics.ShortLinkStatsAccessDailyRespDTO;
import org.ucd.shortlink.project.dto.resp.metrics.ShortLinkStatsAccessRecordRespDTO;
import org.ucd.shortlink.project.dto.resp.metrics.ShortLinkStatsBrowserRespDTO;
import org.ucd.shortlink.project.dto.resp.metrics.ShortLinkStatsDeviceRespDTO;
import org.ucd.shortlink.project.dto.resp.metrics.ShortLinkStatsLocaleRespDTO;
import org.ucd.shortlink.project.dto.resp.metrics.ShortLinkStatsNetworkRespDTO;
import org.ucd.shortlink.project.dto.resp.metrics.ShortLinkStatsOsRespDTO;
import org.ucd.shortlink.project.dto.resp.metrics.ShortLinkStatsRespDTO;
import org.ucd.shortlink.project.dto.resp.metrics.ShortLinkStatsTopIpRespDTO;
import org.ucd.shortlink.project.dto.resp.metrics.ShortLinkStatsUvRespDTO;
import org.ucd.shortlink.project.micrometer.common.constant.MicrometerMetricsConstants;
import org.ucd.shortlink.project.prometheus.common.constant.PrometheusConstants;
import org.ucd.shortlink.project.prometheus.dto.PrometheusQueryReqDTO;
import org.ucd.shortlink.project.prometheus.dto.PrometheusRespDTO;
import org.ucd.shortlink.project.prometheus.service.PrometheusService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Short link monitor interface implementor
 */
@Service("PrometheusStatsService")
@RequiredArgsConstructor
public class ShortLinkStatsPromServiceImpl implements ShortLinkStatsService {
    private PrometheusService prometheusService;

    @Override
    public ShortLinkStatsRespDTO oneShortLinkStats(ShortLinkStatsReqDTO requestParam) {
        ShortLinkStatsRespDTO resp = ShortLinkStatsRespDTO.builder().build();

        String gid = requestParam.getGid();
        String fullShortUrl = requestParam.getFullShortLink();
        String startDate = requestParam.getStartDate();
        String endDate = requestParam.getEndDate();

        // Agg total PV, UV, UIP
        resp.setPv(queryMetricTotal(MicrometerMetricsConstants.METRIC_NAME_SHORTLINK_PAGE_VIEWS_TOTAL,
                gid, fullShortUrl, startDate, endDate));
        resp.setUv(queryMetricTotal(MicrometerMetricsConstants.METRIC_NAME_SHORTLINK_UNIQUE_USERS_TOTAL,
                gid, fullShortUrl, startDate, endDate));
        resp.setUip(queryMetricTotal(MicrometerMetricsConstants.METRIC_NAME_SHORTLINK_UNIQUE_IP_TOTAL,
                gid, fullShortUrl, startDate, endDate));

        // Daily stats
        resp.setDaily(queryDailyStats(gid, fullShortUrl, startDate, endDate));

        // Locale stats
        resp.setLocaleStats(queryLabelStats(MicrometerMetricsConstants.METRIC_NAME_SHORTLINK_LOCALE_TYPE_TOTAL,
                gid, fullShortUrl, startDate, endDate, "locale"));

        // Top IP stats
        resp.setTopIpStats(queryLabelStats(MicrometerMetricsConstants.METRIC_NAME_SHORTLINK_IP_HITS_TOTAL,
                gid, fullShortUrl, startDate, endDate, "ip"));

        // Browser stats
        resp.setBrowserStats(queryLabelStats(MicrometerMetricsConstants.METRIC_NAME_SHORTLINK_BROWSER_TYPE_TOTAL,
                gid, fullShortUrl, startDate, endDate, "browser"));

        // OS stats
        resp.setOsStats(queryLabelStats(MicrometerMetricsConstants.METRIC_NAME_SHORTLINK_OS_TYPE_TOTAL,
                gid, fullShortUrl, startDate, endDate, "os"));

        // Device stats
        resp.setDeviceStats(queryLabelStats(MicrometerMetricsConstants.METRIC_NAME_SHORTLINK_DEVICE_TYPE_TOTAL,
                gid, fullShortUrl, startDate, endDate, "device"));

        // Network stats
        resp.setNetworkStats(queryLabelStats(MicrometerMetricsConstants.METRIC_NAME_SHORTLINK_NETWORK_TYPE_TOTAL,
                gid, fullShortUrl, startDate, endDate, "network"));

        // UV type stats
        resp.setUvTypeStats(queryLabelStats(MicrometerMetricsConstants.METRIC_NAME_SHORTLINK_UNIQUE_USERS_TOTAL,
                gid, fullShortUrl, startDate, endDate, "uvType"));

        // Hourly stats
        resp.setHourStats(queryHourlyStats(gid, fullShortUrl, startDate, endDate));

        // Weekday stats
        resp.setWeekdayStats(queryWeekdayStats(gid, fullShortUrl, startDate, endDate));

        return resp;
    }

    @Override
    public IPage<ShortLinkStatsAccessRecordRespDTO> shortLinkStatsAccessRecord(ShortLinkStatsAccessRecordReqDTO requestParam) {
        // Initialize result page
        IPage<ShortLinkStatsAccessRecordRespDTO> respDTOPage =
                new Page<>(requestParam.getCurrent(), requestParam.getSize());

        if (StrUtil.isBlank(requestParam.getFullShortUrl()) || StrUtil.isBlank(requestParam.getGid())) {
            respDTOPage.setRecords(Collections.emptyList());
            return respDTOPage;
        }

        // Prepare Prometheus query
        PrometheusQueryReqDTO queryReq = PrometheusQueryReqDTO.builder()
                .metricName(MicrometerMetricsConstants.METRIC_NAME_SHORTLINK_PAGE_VIEWS_TOTAL)
                .vertexName(requestParam.getFullShortUrl())
                .job(MicrometerMetricsConstants.JOB_NAME_SHORTLINK_PROJECT)
                .startDate(requestParam.getStartDate())
                .endDate(requestParam.getEndDate())
                .step(PrometheusConstants.PROMETHEUS_DEFAULT_STEP)
                .build();

        PrometheusRespDTO respDTO = prometheusService.queryPrometheusMetric(queryReq);
        List<ShortLinkStatsAccessRecordRespDTO> records = new ArrayList<>();
        if (respDTO != null && "success".equals(respDTO.getStatus()) && respDTO.getData() != null) {
            for (PrometheusRespDTO.MetricResultDTO metricResult : respDTO.getData().getResult()) {
                Map<String, String> labels = metricResult.getMetric();

                List<List<Double>> values = metricResult.getValues(); // [[timestamp, value], ...]
                if (values != null) {
                    for (List<Double> tsVal : values) {
                        if (tsVal.size() < 2) continue;

                        ShortLinkStatsAccessRecordRespDTO record = ShortLinkStatsAccessRecordRespDTO.builder()
                                .uvType(labels.getOrDefault("uvType", "unknown"))
                                .browser(labels.getOrDefault("browser", "unknown"))
                                .os(labels.getOrDefault("os", "unknown"))
                                .device(labels.getOrDefault("device", "unknown"))
                                .locale(labels.getOrDefault("locale", "unknown"))
                                .ip(labels.getOrDefault("ip", "unknown"))
                                .user(labels.getOrDefault("user", "unknown"))
                                .createTime(new Date(tsVal.get(0).longValue() * 1000)) // timestamp in ms
                                .build();

                        records.add(record);
                    }
                }
            }
        }

        // Map to IPage (simple pagination, could use Page object)
        IPage<ShortLinkStatsAccessRecordRespDTO> page = new Page<>();
        page.setRecords(records);
        page.setTotal(records.size());
        page.setCurrent(requestParam.getCurrent());
        page.setSize(requestParam.getSize());

        return page;
    }

    @Override
    public IPage<ShortLinkStatsAccessRecordRespDTO> groupShortLinkStatsAccessRecord(ShortLinkGroupStatsAccessRecordReqDTO requestParam) {
        // Build Prometheus query
        // Build Prometheus query - filter to specific gid only
        PrometheusQueryReqDTO queryReq = PrometheusQueryReqDTO.builder()
                .metricName(MicrometerMetricsConstants.METRIC_NAME_SHORTLINK_PAGE_VIEWS_TOTAL)
                .job(MicrometerMetricsConstants.JOB_NAME_SHORTLINK_PROJECT)
                .vertexName(requestParam.getGid())      // only this gid
                .startDate(requestParam.getStartDate())
                .endDate(requestParam.getEndDate())
                .aggFunc(PrometheusConstants.PROMETHEUS_AGG_FUN_SUM)   // aggregate counts
                .step(PrometheusConstants.PROMETHEUS_DEFAULT_STEP)
                .build();


        PrometheusRespDTO resp = prometheusService.queryPrometheusMetric(queryReq);
        List<ShortLinkStatsAccessRecordRespDTO> records = new ArrayList<>();

        if (resp != null && "success".equals(resp.getStatus()) && resp.getData() != null) {
            for (PrometheusRespDTO.MetricResultDTO metricResult : resp.getData().getResult()) {
                // Example of extracting timestamped values
                if (metricResult.getValues() != null) {
                    for (List<Double> point : metricResult.getValues()) {
                        ShortLinkStatsAccessRecordRespDTO dto = new ShortLinkStatsAccessRecordRespDTO();
                        dto.setUvType("ALL");   // group-level, so generic
                        dto.setCreateTime(new Date(point.get(0).longValue() * 1000));
                        dto.setUser("ALL");     // generic user
                        dto.setIp("ALL");       // generic IP
                        dto.setBrowser("ALL");  // generic browser
                        dto.setOs("ALL");       // generic OS
                        dto.setDevice("ALL");   // generic device
                        dto.setLocale("ALL");   // generic locale
                        dto.setCreateTime(new Date(point.get(0).longValue() * 1000));
                        records.add(dto);
                    }
                }
            }
        }

        // Wrap into IPage response (assuming simple Page impl)
        Page<ShortLinkStatsAccessRecordRespDTO> page = new Page<>();
        page.setRecords(records);
        page.setTotal(records.size());
        page.setCurrent(1);
        page.setSize(records.size());

        return page;
    }


    // --- query functions ---
    private <T> List<T> queryLabelStats(String metricName, String gid, String fullShortUrl,
                                        String startDate, String endDate, String label) {
        PrometheusQueryReqDTO queryReq = PrometheusQueryReqDTO.builder()
                .metricName(metricName)
                .job(MicrometerMetricsConstants.JOB_NAME_SHORTLINK_PROJECT)
                .vertexName(fullShortUrl)
                .startDate(startDate)
                .endDate(endDate)
                .aggFunc(PrometheusConstants.PROMETHEUS_AGG_FUN_SUM)
                .aggBy("gid,fullShortUrl," + label)
                .step(PrometheusConstants.PROMETHEUS_DEFAULT_STEP)
                .build();

        PrometheusRespDTO resp = prometheusService.queryPrometheusMetric(queryReq);

        if (resp == null || resp.getData() == null) return Collections.emptyList();

        // Compute total for ratio calculation
        int total = resp.getData().getResult().stream()
                .mapToInt(metric -> sumMetricValues(metric.getValues(), metric.getValue()))
                .sum();

        List<T> list = new ArrayList<>();

        for (PrometheusRespDTO.MetricResultDTO metric : resp.getData().getResult()) {
            int cnt = sumMetricValues(metric.getValues(), metric.getValue());
            double ratio = total > 0 ? cnt * 1.0 / total : 0;

            switch (label) {
                case "locale":
                    list.add((T) new ShortLinkStatsLocaleRespDTO(cnt, metric.getMetric().get(label), ratio));
                    break;
                case "ip":
                    list.add((T) new ShortLinkStatsTopIpRespDTO(cnt, metric.getMetric().get(label)));
                    break;
                case "browser":
                    list.add((T) new ShortLinkStatsBrowserRespDTO(cnt, metric.getMetric().get(label), ratio));
                    break;
                case "os":
                    list.add((T) new ShortLinkStatsOsRespDTO(cnt, metric.getMetric().get(label), ratio));
                    break;
                case "device":
                    list.add((T) new ShortLinkStatsDeviceRespDTO(cnt, metric.getMetric().get(label), ratio));
                    break;
                case "network":
                    list.add((T) new ShortLinkStatsNetworkRespDTO(cnt, metric.getMetric().get(label), ratio));
                    break;
                case "uvType":
                    list.add((T) new ShortLinkStatsUvRespDTO(cnt, metric.getMetric().get(label), ratio));
                    break;
            }
        }
        return list;
    }

    private int sumMetricValues(List<List<Double>> values, List<Double> value) {
        if (values != null && !values.isEmpty()) {
            return values.stream().mapToInt(v -> v.get(1).intValue()).sum();
        } else if (value != null && !value.isEmpty()) {
            return value.get(1).intValue();
        }
        return 0;
    }

    private List<ShortLinkStatsAccessDailyRespDTO> queryDailyStats(String gid, String fullShortUrl,
                                                                   String startDate, String endDate) {
        return queryTimeSeriesStats(MicrometerMetricsConstants.METRIC_NAME_SHORTLINK_PAGE_VIEWS_TOTAL,
                gid, fullShortUrl, startDate, endDate, "1d");
    }

    private List<Integer> queryHourlyStats(String gid, String fullShortUrl, String startDate, String endDate) {
        List<ShortLinkStatsAccessDailyRespDTO> daily = queryTimeSeriesStats(
                MicrometerMetricsConstants.METRIC_NAME_SHORTLINK_PAGE_VIEWS_TOTAL,
                gid, fullShortUrl, startDate, endDate, "1h"
        );
        return daily.stream().map(ShortLinkStatsAccessDailyRespDTO::getPv).collect(Collectors.toList());
    }

    private List<Integer> queryWeekdayStats(String gid, String fullShortUrl, String startDate, String endDate) {
        List<ShortLinkStatsAccessDailyRespDTO> daily = queryDailyStats(gid, fullShortUrl, startDate, endDate);
        List<Integer> weekdayStats = new ArrayList<>(Collections.nCopies(7, 0));

        for (ShortLinkStatsAccessDailyRespDTO day : daily) {
            int weekday = LocalDate.parse(day.getDate()).getDayOfWeek().getValue() % 7; // 0=Sunday
            weekdayStats.set(weekday, weekdayStats.get(weekday) + day.getPv());
        }
        return weekdayStats;
    }

    private List<ShortLinkStatsAccessDailyRespDTO> queryTimeSeriesStats(String metricName, String gid,
                                                                        String fullShortUrl,
                                                                        String startDate, String endDate,
                                                                        String step) {
        PrometheusQueryReqDTO queryReq = PrometheusQueryReqDTO.builder()
                .metricName(metricName)
                .job(MicrometerMetricsConstants.JOB_NAME_SHORTLINK_PROJECT)
                .vertexName(fullShortUrl)
                .startDate(startDate)
                .endDate(endDate)
                .aggFunc(PrometheusConstants.PROMETHEUS_AGG_FUN_SUM)
                .aggBy("gid,fullShortUrl")
                .step(step)
                .build();

        PrometheusRespDTO resp = prometheusService.queryPrometheusMetric(queryReq);
        List<ShortLinkStatsAccessDailyRespDTO> list = new ArrayList<>();

        if (resp != null && resp.getData() != null) {
            for (PrometheusRespDTO.MetricResultDTO metric : resp.getData().getResult()) {
                for (List<Double> valuePair : metric.getValues()) {
                    ShortLinkStatsAccessDailyRespDTO dto = new ShortLinkStatsAccessDailyRespDTO();
                    dto.setDate(Instant.ofEpochSecond(valuePair.get(0).longValue())
                            .atZone(ZoneId.of("GMT+8")).toLocalDate().toString());
                    dto.setPv(valuePair.get(1).intValue());
                    // UV / UIP can be queried similarly if separate metric names exist
                    list.add(dto);
                }
            }
        }
        return list;
    }

    // Total metric for a gid + fullShortUrl
    private Integer queryMetricTotal(String metricName, String gid, String fullShortUrl,
                                     String startDate, String endDate) {
        PrometheusQueryReqDTO queryReq = PrometheusQueryReqDTO.builder()
                .metricName(metricName)
                .job(MicrometerMetricsConstants.JOB_NAME_SHORTLINK_PROJECT)
                .vertexName(fullShortUrl) // or fullShortUrl label
                .startDate(startDate)
                .endDate(endDate)
                .aggFunc(PrometheusConstants.PROMETHEUS_AGG_FUN_SUM)
                .aggBy("gid,fullShortUrl")
                .step(PrometheusConstants.PROMETHEUS_DEFAULT_STEP)
                .build();

        PrometheusRespDTO resp = prometheusService.queryPrometheusMetric(queryReq);

        return sumPrometheusMetric(resp);
    }

    // Convert PrometheusRespDTO to Integer sum
    private Integer sumPrometheusMetric(PrometheusRespDTO resp) {
        if (resp == null || !"success".equals(resp.getStatus()) || resp.getData() == null) {
            return 0;
        }
        return resp.getData().getResult().stream()
                .mapToInt(metric -> {
                    if (metric.getValues() != null && !metric.getValues().isEmpty()) {
                        double sum = metric.getValues().stream()
                                .mapToDouble(v -> v.get(1))
                                .sum();
                        return (int) sum;  // cast double → int
                    } else if (metric.getValue() != null && !metric.getValue().isEmpty()) {
                        return metric.getValue().get(1).intValue();
                    }
                    return 0;
                })
                .sum();
    }
}
