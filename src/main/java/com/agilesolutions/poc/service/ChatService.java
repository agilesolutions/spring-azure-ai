package com.agilesolutions.poc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatClient aiClient;

    private final VectorStore store;


    public String getBestDeal() {

        BeanOutputConverter<String> outputConverter = new BeanOutputConverter<>(String.class);
        PromptTemplate pt = new PromptTemplate("""
                {query}.
                Which {target} is the most % growth?
                The 0 element in the prices table is the latest price, while the last element is the oldest price.
                """);

        Prompt p = pt.create(
                Map.of("query", "Find the most growth trends",
                        "target", "share")
        );

        return aiClient.prompt(p)
                .advisors(new QuestionAnswerAdvisor(store))
                .call()
                .content();
    }

}
