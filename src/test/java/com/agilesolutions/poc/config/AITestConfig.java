package com.agilesolutions.poc.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.evaluation.FactCheckingEvaluator;
import org.springframework.ai.evaluation.RelevancyEvaluator;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.management.ModelManagementOptions;
import org.springframework.ai.ollama.management.PullModelStrategy;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.chromadb.ChromaDBContainer;
import org.testcontainers.ollama.OllamaContainer;

@Configuration
public class AITestConfig {

    private static final String OLLAMA_BASE_URL = "localhost:11434";

    /**
     * ChromaDB container for vector storage and semantic search operations.
     */
    @Bean
    @ServiceConnection
    public ChromaDBContainer chromaDB() {
        return new ChromaDBContainer("chromadb/chroma:0.5.20");
    }

    /**
     * Ollama container for running embedding model to convert plaintext data to
     * vector representation.
     */
    @Bean
    @ServiceConnection
    public OllamaContainer ollama() {
        return new OllamaContainer("langchain4j/ollama-tinyllama:latest");
    }

    @Bean
    public DynamicPropertyRegistrar dynamicPropertyRegistrar(OllamaContainer ollamaContainer) {
        return registry -> {
            registry.add("spring.ai.ollama.base-url", ollamaContainer::getEndpoint);
        };
    }

    @Bean
    public ChatClient contentEvaluator(
            OllamaApi olamaApi,
            @Value("${com.agilesolutions.evaluation.model}") String evaluationModel
    ) {
        ChatModel chatModel = OllamaChatModel.builder()
                .ollamaApi(olamaApi)
                .defaultOptions(OllamaOptions.builder()
                        .model(evaluationModel)
                        .build())
                .modelManagementOptions(ModelManagementOptions.builder()
                        .pullModelStrategy(PullModelStrategy.WHEN_MISSING)
                        .build())
                .build();
        return ChatClient.builder(chatModel)
                .build();
    }

    @Bean
    public FactCheckingEvaluator factCheckingEvaluator(
            @Qualifier("contentEvaluator") ChatClient chatClient) {
        return new FactCheckingEvaluator(chatClient.mutate());
    }

    @Bean
    public RelevancyEvaluator relevancyEvaluator(
            @Qualifier("contentEvaluator") ChatClient chatClient) {
        return new RelevancyEvaluator(chatClient.mutate());
    }

}
