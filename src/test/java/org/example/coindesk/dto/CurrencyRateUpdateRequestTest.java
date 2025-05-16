package org.example.coindesk.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CurrencyRateUpdateRequestTest {

    @Test
    void testGetAndSetChineseName() {
        CurrencyRateUpdateRequest request = new CurrencyRateUpdateRequest();
        String expectedChineseName = "美元";

        request.setChineseName(expectedChineseName);
        String actualChineseName = request.getChineseName();

        assertEquals(expectedChineseName, actualChineseName, "ChineseName should match the set value");
    }

    @Test
    void testDefaultChineseNameIsNull() {
        CurrencyRateUpdateRequest request = new CurrencyRateUpdateRequest();

        assertNull(request.getChineseName(), "Default ChineseName should be null");
    }
} 