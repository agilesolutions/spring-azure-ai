package com.agilesolutions.poc.loader;

import com.agilesolutions.poc.model.DailyStockData;
import com.agilesolutions.poc.model.Stock;
import com.agilesolutions.poc.model.StockData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
public class StockLoader implements InitializingBean  {

    private final ObjectMapper mapper = new ObjectMapper();

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private VectorStore store;

    @Override
    public void afterPropertiesSet() throws Exception {

        final List<String> companies = List.of("AAPL", "MSFT", "GOOG", "AMZN", "META", "NVDA");
        for (String company : companies) {
            StockData data = restTemplate.getForObject("https://api.twelvedata.com/time_series?symbol={0}&interval=1min&apikey=demo&source=docs",
                    StockData.class,
                    company);
            if (data != null && data.getValues() != null) {
                var list = data.getValues().stream().map(DailyStockData::getClose).toList();
                var doc = Document.builder()
                        .id(company)
                        .metadata("company", company)
                        .text(mapper.writeValueAsString(new Stock(company, list)))
                        .build();
                store.add(new TokenTextSplitter().split(List.of(doc)));
            }
        }
    }
}
