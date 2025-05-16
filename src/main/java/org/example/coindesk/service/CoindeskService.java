package org.example.coindesk.service;

import org.example.coindesk.dto.CoindeskResponse;
import org.example.coindesk.dto.TransformedResponse;
import org.example.coindesk.entity.Currency;
import org.example.coindesk.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CoindeskService {

    private CurrencyRepository currencyRepository;

    @Autowired
    public CoindeskService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String COINDESK_API_URL = "https://kengp3.github.io/blog/coindesk.json";

    public CoindeskResponse fetchRawData() {
        return restTemplate.getForObject(COINDESK_API_URL, CoindeskResponse.class);
    }

    public TransformedResponse transformData() {
        CoindeskResponse raw = fetchRawData();
        
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("MMM d, yyyy HH:mm:ss z", Locale.ENGLISH);
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = inputFormat.parse(raw.time.updated);
            
            List<TransformedResponse.CurrencyInfo> list = raw.bpi.values().stream().map(b -> {
                TransformedResponse.CurrencyInfo info = new TransformedResponse.CurrencyInfo();
                info.code = b.code;
                info.rateFloat = b.rateFloat;

                Currency currency = currencyRepository.findById(b.code)
                    .orElse(new Currency());

                if (currency.getChineseName() == null) {
                    currency.setChineseName("");
                }
                else {
                    String chineseName = currency.getChineseName();
                    currency.setChineseName(chineseName);
                }
                
                info.chineseName = currency.getChineseName();
                return info;
            }).collect(Collectors.toList());

            TransformedResponse result = new TransformedResponse();
            result.updatedTime = outputFormat.format(date);
            result.currencies = list;
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing date", e);
        }
    }

    public Currency saveCurrencyByPost(Currency currency) {
        if (currency == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }

        if (currency.getCode() == null || currency.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Currency code is required");
        }

        Currency existingCurrency = currencyRepository.findById(currency.getCode())
                .orElse(new Currency());

        existingCurrency.setCode(currency.getCode());
        existingCurrency.setChineseName(currency.getChineseName());
        
        return currencyRepository.save(existingCurrency);
    }
}
