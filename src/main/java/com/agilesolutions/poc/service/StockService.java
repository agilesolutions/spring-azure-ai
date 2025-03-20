package com.agilesolutions.poc.service;

import com.agilesolutions.poc.model.DailyStockData;
import com.agilesolutions.poc.model.Stock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {

    private final ObjectMapper mapper = new ObjectMapper();

    private final VectorStore store;

    @Qualifier("twelveDataClient")
    private final WebClient webClient;

    @Value("${STOCK_API_KEY:DEMO}")
    private String apiKey;

    /**
     * see https://support.twelvedata.com/en/articles/5335783-trial
     * see https://api.twelvedata.com/time_series?symbol=AAPL&interval=1min&apikey=demo&source=docs
     * see https://twelvedata.com/docs#core-data
     * @return
     * @throws JsonProcessingException
     */
    public void loadStocks() throws JsonProcessingException {

        final List<String> companies = List.of("AAPL", "MSFT", "GOOG", "AMZN", "META", "NVDA");

        List<DailyStockData> stocks = Collections.emptyList();

        for (String company : companies) {
            stocks = webClient
                                .get()
                    //https://api.twelvedata.com/time_series?symbol=AAPL&interval=1min&apikey=demo&source=docs
                    .uri(builder -> builder.path("/time_series").queryParam("symbol",company).queryParam("apikey",apiKey).build())
                    .headers(h -> h.setBearerAuth(apiKey))
                    .retrieve()
                    .bodyToFlux(DailyStockData.class)
                    .onErrorResume( e -> {
                        // Fallback logic in case of an error
                        log.error("Error occurred: {}", e.getMessage());
                        return null;
                    })
                    .collectList()
                    .block();

            if (stocks != null) {
                var list = stocks.stream().map(DailyStockData::getClose).toList();
                var doc = Document.builder()
                        .id(company)
                        .text(mapper.writeValueAsString(new Stock(company, list)))
                        .build();
                store.add(List.of(doc));
                log.info("Document added: {}", company);
            }
        }

    }
}
