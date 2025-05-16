package org.example.coindesk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class CoindeskResponse {
    public Time time;
    public String disclaimer;
    public String chartName;
    public Map<String, BpiDetail> bpi;

    public static class Time {
        public String updated;
        public String updatedISO;
        public String updateduk;
    }

    public static class BpiDetail {
        public String code;
        public String symbol;
        public String rate;
        public String description;
        @JsonProperty("rate_float")
        public String rateFloat;
    }
}
