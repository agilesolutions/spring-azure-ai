package com.agilesolutions.poc.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.chromadb.ChromaDBContainer;
import org.testcontainers.ollama.OllamaContainer;

@Configuration
public class ChromaDBConfiguration {

    @Bean
    @ServiceConnection
    public ChromaDBContainer chromaDB() {
        return new ChromaDBContainer("chromadb/chroma:0.5.20");
    }

    @Bean
    @ServiceConnection
    public OllamaContainer ollama() {


        return new OllamaContainer("ollama/ollama:0.4.5");
    }
}