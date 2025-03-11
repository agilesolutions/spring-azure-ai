package com.agilesolutions.poc.service;

import com.agilesolutions.poc.config.AIConfig;
import com.agilesolutions.poc.config.RestConfig;
import com.agilesolutions.poc.model.DailyStockData;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AIConfig.class, RestConfig.class, StockService.class})
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