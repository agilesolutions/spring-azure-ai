package com.agilesolutions.poc.docker;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.chromadb.ChromaDBContainer;
import org.testcontainers.ollama.OllamaContainer;

public class BaseIntegrationTest {

    @ServiceConnection
    protected static final ChromaDBContainer vectorStoreContainer = new ChromaDBContainer("chromadb/chroma:0.5.20")
            .withReuse(true);


    @ServiceConnection
    protected static final OllamaContainer modelContainer = new OllamaContainer("langchain4j/ollama-tinyllama:latest")
            .withReuse(true);


    static {
        vectorStoreContainer.start();
        modelContainer.start();
    }

}