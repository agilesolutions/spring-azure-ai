package com.agilesolutions.poc.service;

import com.agilesolutions.poc.tools.StockTools;
import com.agilesolutions.poc.tools.WalletTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgentService {

    private final ChatClient aiClient;

    private final StockTools stockTools;

    private final WalletTools walletTools;

    public String calculateWalletValueWithTools() {
        PromptTemplate pt = new PromptTemplate("""
        What’s the current value in dollars of my wallet based on the latest stock daily prices ?
        """);

        return this.aiClient.prompt(pt.create())
                .tools(stockTools, walletTools)
                .call()
                .content();
    }

    public String calculateHighestWalletValue(@PathVariable int days) {
        PromptTemplate pt = new PromptTemplate("""
        On which day during last {days} days my wallet had the highest value in dollars based on the historical daily stock prices ?
        """);

        return this.aiClient.prompt(pt.create(Map.of("days", days)))
                .tools(stockTools, walletTools)
                .call()
                .content();
    }
}
