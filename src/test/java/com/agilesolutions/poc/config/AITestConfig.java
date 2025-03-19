package com.agilesolutions.poc.config;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.search.documents.indexes.SearchIndexClient;
import com.azure.search.documents.indexes.SearchIndexClientBuilder;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.azure.AzureVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AITestConfig {


    @Bean
    public SearchIndexClient searchIndexClient() {
        return new SearchIndexClientBuilder().endpoint(System.getenv("AZURE_AI_SEARCH_ENDPOINT"))
                .credential(new AzureKeyCredential(System.getenv("AZURE_AI_SEARCH_API_KEY")))
                .buildClient();
    }

    @Bean("azureVectorStore")
    public VectorStore azureVectorStore(SearchIndexClient searchIndexClient, EmbeddingModel embeddingModel) {
        AzureVectorStore.builder(searchIndexClient, embeddingModel)
                .initializeSchema(true)
                // Define the metadata fields to be used
                // in the similarity search filters.
                .filterMetadataFields(List.of(AzureVectorStore.MetadataField.text("country"), AzureVectorStore.MetadataField.int64("year"),
                        AzureVectorStore.MetadataField.date("activationDate")))
                .defaultTopK(5)
                .defaultSimilarityThreshold(0.7)
                .indexName("spring-ai-document-index")
                .build();
    }


}
