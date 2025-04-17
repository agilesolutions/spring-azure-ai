package com.agilesolutions.poc.rest;


import com.agilesolutions.poc.config.AITestConfig;
import com.agilesolutions.poc.service.StockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(StockController.class)
@ContextConfiguration(classes = {AITestConfig.class, StockService.class})
class OpenAIControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private StockService stockService;

    @Test
    public void givenGetCatHaiku_whenCallingAiClient_thenCorrect() throws Exception {
        mockMvc.perform(get("/ai/bom").param("version", "1.24")
                        .contentType(MediaType.APPLICATION_JSON))
                //.andExpect(status().isOk())
                .andDo(print());
        //.andExpect(content().string(containsStringIgnoringCase("cat")));
    }
}

