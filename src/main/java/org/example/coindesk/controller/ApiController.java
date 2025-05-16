package org.example.coindesk.controller;

import org.example.coindesk.dto.CoindeskResponse;
import org.example.coindesk.dto.CurrencyRateUpdateRequest;
import org.example.coindesk.dto.TransformedResponse;
import org.example.coindesk.entity.Currency;
import org.example.coindesk.repository.CurrencyRepository;
import org.example.coindesk.service.CoindeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private CurrencyRepository repository;
    private CoindeskService service;

    @Autowired
    public ApiController(CurrencyRepository repository, CoindeskService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping("/coindesk/raw")
    public CoindeskResponse getRaw() {
        return service.fetchRawData();
    }

    @GetMapping("/coindesk/transform")
    public TransformedResponse getTransformed() {
        return service.transformData();
    }

    @GetMapping("/coindesk/currencies")
    public List<Currency> getAll() {
        return repository.findAll();
    }

    @GetMapping("/coindesk/currencies/{code}")
    public Currency getByCode(@PathVariable(name = "code") String code) {
        return repository.findById(code)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Currency not found with code: " + code));
    }

    @PostMapping("/currencies/save")
    public Currency createCurrency(@RequestBody Currency currency) {
        return service.saveCurrencyByPost(currency);
    }

    @PutMapping("/currencies/{code}")
    public Currency updateCurrencyName(@PathVariable(name = "code") String code, @RequestBody CurrencyRateUpdateRequest request) {
        Currency currency = repository.findById(code)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Currency not found with code: " + code));
        currency.setChineseName(request.getChineseName());
        return repository.save(currency);
    }

    @DeleteMapping("/currencies/{code}")
    public void delete(@PathVariable(name = "code") String code) {
        repository.deleteById(code);
    }
}

