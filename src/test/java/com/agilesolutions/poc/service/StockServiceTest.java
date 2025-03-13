package com.agilesolutions.poc.service;

import com.agilesolutions.poc.config.AITestConfig;
import com.agilesolutions.poc.config.RestTestConfig;
import com.agilesolutions.poc.model.DailyStockData;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig(classes = {AITestConfig.class, RestTestConfig.class, StockService.class}, initializers = {ConfigDataApplicationContextInitializer.class})
@TestPropertySource(properties = { "spring.config.location=classpath:application.yaml" })
class StockServiceTest {

    @Autowired
    StockService stockService;

    @Test
    public void givenAvailable_whenRetrieving_thenReturnStocks() throws JsonProcessingException {

        List<DailyStockData> stocks = stockService.loadStocks();

        assertAll("verify result",
                () -> assertTrue(stocks.size() > 0)
        );


    }

}