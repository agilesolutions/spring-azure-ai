package com.agilesolutions.poc.rest;

import com.agilesolutions.poc.service.AgentService;
import com.agilesolutions.poc.service.ChatService;
import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.MetricTelemetry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("agent")
public class AgentController {

    private final AgentService agentService;

    private final TelemetryClient telemetryClient;

    @GetMapping("/with-tools")
    public ResponseEntity<String> calculateWalletValueWithTools() {

        // measure Azure client search query benchmark
        long startTime = System.nanoTime();
        ResponseEntity<String> response = ResponseEntity.ok(agentService.calculateWalletValueWithTools());
        long endTime = System.nanoTime();

        MetricTelemetry benchmark = new MetricTelemetry();
        benchmark.setName("DB query");
        benchmark.setValue(endTime - startTime);
        telemetryClient.trackMetric(benchmark);

        return response;
    }

    @GetMapping("/highest-day/{days}")
    public ResponseEntity<String> calculateHighestWalletValue(@PathVariable int days) {

        // measure Azure client search query benchmark
        long startTime = System.nanoTime();
        ResponseEntity<String> response = ResponseEntity.ok(agentService.calculateHighestWalletValue(days));
        long endTime = System.nanoTime();

        MetricTelemetry benchmark = new MetricTelemetry();
        benchmark.setName("DB query");
        benchmark.setValue(endTime - startTime);
        telemetryClient.trackMetric(benchmark);

        return response;

    }
}
