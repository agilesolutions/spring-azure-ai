package com.agilesolutions.poc.schedule;

import com.agilesolutions.poc.service.StockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockScheduler {

    private final StockService stockService;

    @Scheduled(cron = "0 */1 * ? * *")
    public void scheduleTask() throws JsonProcessingException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm:ss.SSS");

        String strDate = dateFormat.format(new Date());

        log.info("Cron job Scheduler: Job running at - {}", strDate);

        stockService.loadStocks();
    }
}