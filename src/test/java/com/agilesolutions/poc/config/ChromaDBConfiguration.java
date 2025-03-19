package com.agilesolutions.poc.config;

import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaVectorStore;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.testcontainers.chromadb.ChromaDBContainer;
import org.testcontainers.ollama.OllamaContainer;

@Configuration
public class ChromaDBConfiguration {

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
        return new OllamaContainer("ollama/ollama:0.3.12");
    }



    @Bean
    public ChromaApi chromaApi(RestClient.Builder restClientBuilder) {
        String chromaUrl = "http://localhost:8000";
        ChromaApi chromaApi = new ChromaApi(chromaUrl, restClientBuilder);
        return chromaApi;
    }

    @Bean
    public OllamaApi ollamaApi() {
        return new OllamaApi("OLLAMA_BASE_URL");
    }


    @Bean
    public EmbeddingModel embeddingModel(OllamaApi ollamaApi) {
        return OllamaEmbeddingModel.builder().ollamaApi(ollamaApi).build();
    }

    @Bean
    public VectorStore chromaVectorStore(EmbeddingModel embeddingModel, ChromaApi chromaApi) {
        return ChromaVectorStore.builder(chromaApi, embeddingModel)
                .collectionName("TestCollection")
                .initializeSchema(true)
                .build();
    }


}