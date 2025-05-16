package org.example.coindesk.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

class CoindeskResponseTest {

    @Test
    void testCoindeskResponse() {
        CoindeskResponse response = new CoindeskResponse();
        response.time = new CoindeskResponse.Time();
        response.time.updated = "Mar 11, 2024 09:30:00 UTC";

        response.bpi = new HashMap<>();

        CoindeskResponse.BpiDetail usdBpi = new CoindeskResponse.BpiDetail();
        usdBpi.code = "USD";
        usdBpi.symbol = "&#36;";
        usdBpi.rate = "68,123.5000";
        usdBpi.description = "United States Dollar";
        usdBpi.rateFloat = "68123.5";

        response.bpi.put("USD", usdBpi);

        assertNotNull(response.time);
        assertEquals("Mar 11, 2024 09:30:00 UTC", response.time.updated);

        assertNotNull(response.bpi);
        assertEquals(1, response.bpi.size());
        assertTrue(response.bpi.containsKey("USD"));

        CoindeskResponse.BpiDetail savedBpi = response.bpi.get("USD");
        assertEquals("USD", savedBpi.code);
        assertEquals("&#36;", savedBpi.symbol);
        assertEquals("68,123.5000", savedBpi.rate);
        assertEquals("United States Dollar", savedBpi.description);
        assertEquals("68123.5", savedBpi.rateFloat);
    }
}