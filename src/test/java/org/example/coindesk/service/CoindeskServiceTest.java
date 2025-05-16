package org.example.coindesk.service;

import org.example.coindesk.dto.CoindeskResponse;
import org.example.coindesk.dto.TransformedResponse;
import org.example.coindesk.entity.Currency;
import org.example.coindesk.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CoindeskServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CoindeskService coindeskService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        Field restTemplateField = CoindeskService.class.getDeclaredField("restTemplate");
        restTemplateField.setAccessible(true);
        restTemplateField.set(coindeskService, restTemplate);
    }

    @Test
    void fetchRawData_ShouldReturnCoindeskResponse() {
        CoindeskResponse mockResponse = new CoindeskResponse();
        mockResponse.time = new CoindeskResponse.Time();
        mockResponse.time.updated = "Mar 11, 2024 09:30:00 UTC";
        mockResponse.bpi = new HashMap<>();
        
        CoindeskResponse.BpiDetail usdBpi = new CoindeskResponse.BpiDetail();
        usdBpi.code = "USD";
        usdBpi.rateFloat = "68123.5";
        mockResponse.bpi.put("USD", usdBpi);

        when(restTemplate.getForObject(anyString(), eq(CoindeskResponse.class)))
            .thenReturn(mockResponse);

        CoindeskResponse result = coindeskService.fetchRawData();

        assertNotNull(result);
        assertEquals("Mar 11, 2024 09:30:00 UTC", result.time.updated);
        assertEquals("USD", result.bpi.get("USD").code);
        assertEquals("68123.5", result.bpi.get("USD").rateFloat);
    }

    @Test
    void transformData_ShouldTransformAndSaveData() {
        CoindeskResponse mockRawResponse = new CoindeskResponse();
        mockRawResponse.time = new CoindeskResponse.Time();
        mockRawResponse.time.updated = "Mar 11, 2024 09:30:00 UTC";
        mockRawResponse.bpi = new HashMap<>();
        
        CoindeskResponse.BpiDetail usdBpi = new CoindeskResponse.BpiDetail();
        usdBpi.code = "USD";
        usdBpi.rateFloat = "68123.5";
        mockRawResponse.bpi.put("USD", usdBpi);

        Currency mockCurrency = new Currency();
        mockCurrency.setCode("USD");
        mockCurrency.setChineseName("美元");

        when(restTemplate.getForObject(anyString(), eq(CoindeskResponse.class)))
            .thenReturn(mockRawResponse);
        when(currencyRepository.findById(anyString()))
            .thenReturn(Optional.of(mockCurrency));
        when(currencyRepository.save(any(Currency.class)))
            .thenReturn(mockCurrency);

        TransformedResponse result = coindeskService.transformData();

        assertNotNull(result);
        assertEquals("2024/03/11 17:30:00", result.updatedTime);
        assertEquals(1, result.currencies.size());
        assertEquals("USD", result.currencies.get(0).code);
        assertEquals("美元", result.currencies.get(0).chineseName);
        assertEquals("68123.5", result.currencies.get(0).rateFloat);
    }

} 