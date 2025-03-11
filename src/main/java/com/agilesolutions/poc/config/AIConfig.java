package com.agilesolutions.poc.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class AIConfig {

    @Value("${spring.ai.openai.api-key}")
    private String openAiKey;


    @Bean("openAiChatClient")
    public ChatClient embeddingModel() {
        return ChatClient.builder(new OpenAiChatModel(new OpenAiApi(""))).build();
    }

    @Bean("simpleVectorStore")
    public VectorStore vectorStore() {
        return SimpleVectorStore.builder(new OpenAiEmbeddingModel(new OpenAiApi(""))).build();
    }

}
