package com.agilesolutions.poc.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class AIConfig {

    @Value("vectorStore.json")
    private String vectorStoreFileName;

    @Value("${spring.ai.openai.api-key}")
    private String openAiKey;

    @Value("classpath:/faq/spring-faq.txt")
    private Resource springFaq;


    @Bean("openAiChatClient")
    public ChatClient embeddingModel(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder.build();
    }


    /**
     * https://wesome.org/spring-ai-simple-vector-store
     * @param embeddingModel
     * @return
     */
    @Bean("simpleVectorStore")
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        File vectorStoreFile = getVectorStoreFile();
        if (vectorStoreFile.exists() && vectorStoreFile.length() != 0) {
            simpleVectorStore.load(vectorStoreFile);
        } else {
            TextReader textReader = new TextReader(springFaq);
            textReader.getCustomMetadata().put("filename", "spring-faq.txt");
            List<Document> documents = textReader.get();
            TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = tokenTextSplitter.apply(documents);
            simpleVectorStore.add(splitDocuments);
            simpleVectorStore.save(vectorStoreFile);
        }
        return simpleVectorStore;
    }

    private File getVectorStoreFile() {
        System.out.println("SpringAiApplication.getVectorStoreFile");
        Path path = Paths.get("src/main/resources/data");
        var vectorStoreFile = path.toFile().getAbsolutePath() + "\\" + vectorStoreFileName;
        return new File(vectorStoreFile);
    }

}
