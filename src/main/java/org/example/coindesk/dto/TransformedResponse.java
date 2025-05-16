package org.example.coindesk.dto;

import java.util.List;

public class TransformedResponse {
    public String updatedTime;
    public List<CurrencyInfo> currencies;

    public static class CurrencyInfo {
        public String code;
        public String chineseName;
        public String rateFloat;
    }
}