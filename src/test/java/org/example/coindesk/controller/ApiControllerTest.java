package org.example.coindesk.controller;

import org.example.coindesk.CoindeskApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        classes = CoindeskApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private void printResponseBody(org.springframework.test.web.servlet.MvcResult result) throws Exception {
        System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    void getCoindeskData() throws Exception {
        mockMvc.perform(get("/api/coindesk/raw"))
                .andDo(this::printResponseBody)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))

                .andExpect(jsonPath("$.time.updated").value("Sep 2, 2024 07:07:20 UTC"))
                .andExpect(jsonPath("$.time.updatedISO").value("2024-09-02T07:07:20+00:00"))
                .andExpect(jsonPath("$.time.updateduk").value("Sep 2, 2024 at 08:07 BST"))

                .andExpect(jsonPath("$.disclaimer").value("just for test"))
                .andExpect(jsonPath("$.chartName").value("Bitcoin"))

                .andExpect(jsonPath("$.bpi.USD.code").value("USD"))
                .andExpect(jsonPath("$.bpi.USD.symbol").value("&#36;"))
                .andExpect(jsonPath("$.bpi.USD.rate").value("57,756.298"))
                .andExpect(jsonPath("$.bpi.USD.description").value("United States Dollar"))
                .andExpect(jsonPath("$.bpi.USD.rate_float").value(57756.2984))

                .andExpect(jsonPath("$.bpi.GBP.code").value("GBP"))
                .andExpect(jsonPath("$.bpi.GBP.symbol").value("&pound;"))
                .andExpect(jsonPath("$.bpi.GBP.rate").value("43,984.02"))
                .andExpect(jsonPath("$.bpi.GBP.description").value("British Pound Sterling"))
                .andExpect(jsonPath("$.bpi.GBP.rate_float").value(43984.0203))

                .andExpect(jsonPath("$.bpi.EUR.code").value("EUR"))
                .andExpect(jsonPath("$.bpi.EUR.symbol").value("&euro;"))
                .andExpect(jsonPath("$.bpi.EUR.rate").value("52,243.287"))
                .andExpect(jsonPath("$.bpi.EUR.description").value("Euro"))
                .andExpect(jsonPath("$.bpi.EUR.rate_float").value(52243.2865));
    }

    @Test
    void getTransformedData() throws Exception {
        mockMvc.perform(get("/api/coindesk/transform"))
                .andDo(this::printResponseBody)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.updatedTime").value("2024/09/02 15:07:20"))
                .andExpect(jsonPath("$.currencies").isArray())
                .andExpect(jsonPath("$.currencies.length()").value(3))

                .andExpect(jsonPath("$.currencies[0].code").value("USD"))
                .andExpect(jsonPath("$.currencies[0].chineseName").value("美元"))
                .andExpect(jsonPath("$.currencies[0].rateFloat").value("57756.2984"))

                .andExpect(jsonPath("$.currencies[1].code").value("GBP"))
                .andExpect(jsonPath("$.currencies[1].chineseName").value("英鎊"))
                .andExpect(jsonPath("$.currencies[1].rateFloat").value("43984.0203"))

                .andExpect(jsonPath("$.currencies[2].code").value("EUR"))
                .andExpect(jsonPath("$.currencies[2].chineseName").value("歐元"))
                .andExpect(jsonPath("$.currencies[2].rateFloat").value("52243.2865"));
    }

    @Test
    void getCurrenciesFromDataBase() throws Exception {
        mockMvc.perform(get("/api/coindesk/currencies"))
                .andDo(this::printResponseBody)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))

                .andExpect(jsonPath("$[0].code").value("USD"))
                .andExpect(jsonPath("$[0].chineseName").value("美元"))

                .andExpect(jsonPath("$[1].code").value("GBP"))
                .andExpect(jsonPath("$[1].chineseName").value("英鎊"))

                .andExpect(jsonPath("$[2].code").value("EUR"))
                .andExpect(jsonPath("$[2].chineseName").value("歐元"));
    }

    @Test
    void saveNewCurrency() throws Exception {
        mockMvc.perform(get("/api/coindesk/currencies/JPY"))
                .andExpect(status().isNotFound());

        String currencyJson = """
                {
                    "code": "JPY",
                    "chineseName": "日圓"
                }
                """;

        System.out.println("The new currency:");
        mockMvc.perform(post("/api/currencies/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(currencyJson))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/coindesk/currencies/JPY"))
                .andDo(this::printResponseBody)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.code").value("JPY"))
                .andExpect(jsonPath("$.chineseName").value("日圓"));

        mockMvc.perform(delete("/api/currencies/JPY"))
                .andExpect(status().isOk());
    }

    @Test
    void updateCurrencyName() throws Exception {
        mockMvc.perform(get("/api/coindesk/transform"))
                .andExpect(status().isOk());

        System.out.println("The origin chinese name of currency:");
        mockMvc.perform(get("/api/coindesk/currencies/USD"))
                .andDo(this::printResponseBody)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.code").value("USD"))
                .andExpect(jsonPath("$.chineseName").value("美元"));

        String updateJson = """
                {
                    "chineseName": "美金"
                }
                """;

        System.out.println("The revised chinese name of currency:");
        mockMvc.perform(put("/api/currencies/USD")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.code").value("USD"))
                .andExpect(jsonPath("$.chineseName").value("美金"));

        mockMvc.perform(get("/api/coindesk/currencies/USD"))
                .andDo(this::printResponseBody)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.code").value("USD"))
                .andExpect(jsonPath("$.chineseName").value("美金"));

        mockMvc.perform(put("/api/currencies/NONEXISTENT")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isNotFound());

        // Clean up
        String undoJson = """
                {
                    "chineseName": "美元"
                }
                """;
        mockMvc.perform(put("/api/currencies/USD")
                .contentType(MediaType.APPLICATION_JSON)
                .content(undoJson))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCurrency() throws Exception {
        String currencyJson = """
                {
                    "code": "JPY",
                    "chineseName": "日圓"
                }
                """;

        mockMvc.perform(post("/api/currencies/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(currencyJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/coindesk/currencies/JPY"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.code").value("JPY"))
                .andExpect(jsonPath("$.chineseName").value("日圓"));

        mockMvc.perform(delete("/api/currencies/JPY"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/coindesk/currencies/JPY"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/currencies/NONEXISTENT"))
                .andExpect(status().isOk());
    }
}
