package org.ucd.shortlink.project.prometheus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrometheusRespDTO {
    private String status;
    private DataDTO data;

    @Data
    public static class DataDTO {
        private String resultType; // "matrix" | "vector" | "scalar" | "string"
        private List<MetricResultDTO> result;
    }

    @Data
    public static class MetricResultDTO {
        private Map<String, String> metric;  // __name__, job, instance, customized labels
        private List<List<Double>> values;   // range query [[timestamp, value], ...]
        private List<Double> value;          // instant query [timestamp, value]
    }
}
