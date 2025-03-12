package com.agilesolutions.poc.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AITestConfig {

    @Value("${spring.ai.openai.api-key:demo}")
    String openAiKey;


    @Bean("openAiChatClient")
    public ChatClient chatClient(ChatMemory chatMemory, @Qualifier("simpleVectorStore") VectorStore vectorStore) {
        OpenAiApi openAiApi = new OpenAiApi.Builder().apiKey(openAiKey).build();
        return ChatClient.builder(OpenAiChatModel.builder().openAiApi(openAiApi).build())
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory),
                        new QuestionAnswerAdvisor(vectorStore))
                .build();
    }

    @Bean("simpleVectorStore")
    public VectorStore vectorStore() {
        OpenAiApi openAiApi = new OpenAiApi.Builder().apiKey(openAiKey).build();
        return SimpleVectorStore.builder(new OpenAiEmbeddingModel(openAiApi)).build();
    }

    /**
     * read https://medium.com/wearewaes/creating-a-chatbot-with-spring-ai-java-and-openai-ee42ed9f29f8
     * @return
     */
    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

}
