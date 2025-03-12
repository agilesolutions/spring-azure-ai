package com.agilesolutions.poc.service;

import com.agilesolutions.poc.model.DailyStockData;
import com.agilesolutions.poc.model.Stock;
import com.agilesolutions.poc.model.StockData;
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
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {

    @Value("${STOCK_API_KEY:DEMO}")
    private String apiKey;

    private final ObjectMapper mapper = new ObjectMapper();

    @Qualifier("simpleVectorStore")
    private final VectorStore store;

    @Qualifier("stockClient")
    private final WebClient webClient;

    /**
     * see https://support.twelvedata.com/en/articles/5335783-trial
     * see https://api.twelvedata.com/time_series?symbol=AAPL&interval=1min&apikey=demo&source=docs
     * see https://twelvedata.com/docs#core-data
     * @return
     * @throws JsonProcessingException
     */
    public List<DailyStockData> loadStocks() throws JsonProcessingException {

        final List<String> companies = List.of("AAPL", "MSFT", "GOOG", "AMZN", "META", "NVDA");

        List<DailyStockData> stocks = Collections.emptyList();

        for (String company : companies) {
            stocks = webClient
                                .get()
                    //https://api.twelvedata.com/time_series?symbol=AAPL&interval=1min&apikey=demo&source=docs
                    .uri(builder -> builder.path("/time_series").queryParam("symbol",company).queryParam("apikey",apiKey).build())
                    //.headers(h -> h.setBearerAuth(apiKey))
                    .header("Authorization", "Bearer " + apiKey)
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

        return stocks;

    }
}
