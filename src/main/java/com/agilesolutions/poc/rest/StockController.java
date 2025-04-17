package com.agilesolutions.poc.rest;


import com.agilesolutions.poc.service.ChatService;
import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.MetricTelemetry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("stocks")
public class StockController {

    private final ChatService chatService;

    private final TelemetryClient telemetryClient;

    @GetMapping("/best-trend")
    public ResponseEntity<String> getBestDeal(@RequestParam("version") String version) {

        // measure Azure client search query benchmark
        long startTime = System.nanoTime();
        ResponseEntity<String> response = ResponseEntity.ok(chatService.getBestDeal());
        long endTime = System.nanoTime();

        MetricTelemetry benchmark = new MetricTelemetry();
        benchmark.setName("DB query");
        benchmark.setValue(endTime - startTime);
        telemetryClient.trackMetric(benchmark);

        return response;
    }


}
