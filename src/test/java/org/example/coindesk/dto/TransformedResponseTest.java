package org.example.coindesk.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransformedResponseTest {

    @Test
    void testTransformedResponse() {
        TransformedResponse response = new TransformedResponse();
        response.updatedTime = "2024/03/11 09:30:00";
        
        TransformedResponse.CurrencyInfo currencyInfo = new TransformedResponse.CurrencyInfo();
        currencyInfo.code = "USD";
        currencyInfo.chineseName = "美元";
        currencyInfo.rateFloat = "68123.5";
        
        response.currencies = java.util.Arrays.asList(currencyInfo);

        assertNotNull(response.updatedTime);
        assertEquals("2024/03/11 09:30:00", response.updatedTime);
        assertNotNull(response.currencies);
        assertEquals(1, response.currencies.size());
        
        TransformedResponse.CurrencyInfo savedInfo = response.currencies.get(0);
        assertEquals("USD", savedInfo.code);
        assertEquals("美元", savedInfo.chineseName);
        assertEquals("68123.5", savedInfo.rateFloat);
    }
} 