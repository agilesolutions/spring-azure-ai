package com.agilesolutions.poc.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Value("${spring.ai.openai.api-key}")
    private String openAiKey;


    @Bean("openAiChatClient")
    public ChatClient embeddingModel(ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }

    @Bean("simpleVectorStore")
    public VectorStore vectorStore(SimpleVectorStore.SimpleVectorStoreBuilder builder) {
        return builder.build();
    }

}
