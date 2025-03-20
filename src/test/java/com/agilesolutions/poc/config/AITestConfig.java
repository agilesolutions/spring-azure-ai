package com.agilesolutions.poc.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        return new OllamaContainer("ollama/ollama:0.3.12");
    }



}
