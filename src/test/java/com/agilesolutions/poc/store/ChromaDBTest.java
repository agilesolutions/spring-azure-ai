package com.agilesolutions.poc.store;

import com.agilesolutions.poc.config.ChromaDBConfiguration;
import com.agilesolutions.poc.loader.ChromaDBLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = {ChromaDBConfiguration.class, ChromaDBLoader.class}, initializers = {ConfigDataApplicationContextInitializer.class})
@TestPropertySource(properties = { "spring.config.location=classpath:application.yaml" })
public class ChromaDBTest {

    private static final int MAX_RESULTS = 2;

    @Autowired
    VectorStore store;

    @ParameterizedTest
    @ValueSource(strings = {"Find the most growth trends"})
    void whenSearchingTrends_thenRelevantStocksReturned(String theme) {
        SearchRequest searchRequest = SearchRequest
                .builder()
                .query(theme)
                .topK(MAX_RESULTS)
                .build();

        List<Document> documents = store.similaritySearch(searchRequest);

        assertThat(documents)
                .hasSizeLessThanOrEqualTo(MAX_RESULTS)
                .allSatisfy(document -> {
                    String title = String.valueOf(document.getMetadata().get("title"));
                    assertThat(title)
                            .isNotBlank();
                });
    }

}
