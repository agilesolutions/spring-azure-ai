package com.agilesolutions.poc.service;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.vectorstore.SearchRequest;
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
            Which share is the most % growth?
            The 0 element in the prices table is the latest price, while the last element is the oldest price.
            Return a full name of company instead of a market shortcut. 
            """);

        SearchRequest searchRequest = SearchRequest.builder()
                .query("""
            Find the most growth trends.
            The 0 element in the prices table is the latest price, while the last element is the oldest price.
            """)
                .topK(3)
                .similarityThreshold(0.7)
                .build();

        return aiClient.prompt(pt.create())
                .advisors(new QuestionAnswerAdvisor(store, searchRequest))
                .call()
                .content();

    }

}