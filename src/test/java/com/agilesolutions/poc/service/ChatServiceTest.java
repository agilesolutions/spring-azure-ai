package com.agilesolutions.poc.service;

import com.agilesolutions.poc.config.AITestConfig;
import com.agilesolutions.poc.loader.MarkdownLoader;
import com.agilesolutions.poc.loader.StockLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig(classes = {AITestConfig.class, ChatService.class, StockLoader.class})
class ChatServiceTest {

    @Autowired
    ChatService chatService;

    @Test
    public void givenAvailable_whenRetrieving_thenReturnStocks() throws JsonProcessingException {

        String result = chatService.getBestDeal("");

        assertAll("verify result",
                () -> assertTrue(result.length() > 0)
        );


    }

}