package com.agilesolutions.poc.tools;

import com.agilesolutions.poc.dto.DailyShareQuote;
import com.agilesolutions.poc.dto.StockResponse;
import com.agilesolutions.poc.model.DailyStockData;
import com.agilesolutions.poc.model.StockData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockTools {

    @Qualifier("twelveDataClient")
    private final WebClient webClient;

    @Value("${STOCK_API_KEY:DEMO}")
    private String apiKey;

    /**
     * https://api.twelvedata.com/time_series?symbol=AAPL&interval=1day&outputsize=4&apikey=demo&source=docs
     * @param company
     * @return
     */
    @Tool(description = "Latest stock prices")
    public StockResponse getLatestStockPrices(@ToolParam(description = "Name of company") String company) {
        log.info("Get stock prices for: {}", company);

        DailyStockData latestData =  webClient
                .get()
                //https://api.twelvedata.com/time_series?symbol=AAPL&interval=1min&apikey=demo&source=docs
                .uri(builder -> builder.path("/time_series")
                        .queryParam("symbol",company)
                        .queryParam("interval","1min")
                        .queryParam("outputsize",1)
                        .queryParam("apikey",apiKey).build())
                .headers(h -> h.setBearerAuth(apiKey))
                .retrieve()
                .bodyToFlux(DailyStockData.class)
                .onErrorResume( e -> {
                    // Fallback logic in case of an error
                    log.error("Error occurred: {}", e.getMessage());
                    return null;
                })
                .collectList()
                .block().get(0);

        log.info("Get stock prices ({}) -> {}", company, latestData.getClose());
        return new StockResponse(Float.parseFloat(latestData.getClose()));
    }

    @Tool(description = "Historical daily stock prices")
    public List<DailyShareQuote> getHistoricalStockPrices(@ToolParam(description = "Search period in days") int days,
                                                          @ToolParam(description = "Name of company") String company) {
        log.info("Get historical stock prices: {} for {} days", company, days);

        List<DailyStockData> stocks= webClient
                .get()
                //https://api.twelvedata.com/time_series?symbol=AAPL&interval=1min&apikey=demo&source=docs
                .uri(builder -> builder.path("/time_series")
                        .queryParam("symbol",company)
                        .queryParam("interval","1day")
                        .queryParam("outputsize",days)
                        .queryParam("apikey",apiKey).build())
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

        return stocks.stream()
                .map(d -> new DailyShareQuote(company, Float.parseFloat(d.getClose()), d.getDatetime()))
                .toList();
    }


}
