package com.agilesolutions.poc.service;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class ChatService {

    @Qualifier("openAiChatModel")
    private final ChatClient aiClient;

    @Qualifier("simpleVectorStore")
    private final VectorStore store;

    public String getBestDeal(String version) {

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