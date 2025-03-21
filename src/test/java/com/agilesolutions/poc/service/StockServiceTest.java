package com.agilesolutions.poc.service;

import com.agilesolutions.poc.config.AITestConfig;
import com.agilesolutions.poc.config.RestTestConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
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
    VectorStore store;

    @Autowired
    StockService stockService;

    @Test
    public void givenAvailable_whenRetrieving_thenReturnStocks() throws JsonProcessingException {

        stockService.loadStocks();

        List<Document> documents = store.similaritySearch("Find the most growth trends");

        assertAll("verify result",
                () -> assertTrue(documents.size() > 0)
        );


    }

}