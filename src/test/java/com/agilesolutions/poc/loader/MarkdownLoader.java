package com.agilesolutions.poc.loader;

import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

@Component
public class MarkdownLoader implements InitializingBean {

    private final MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder().build();

    @Autowired
    @Qualifier("simpleVectorStore")
    private VectorStore vectorStore;

    @Override
    public void afterPropertiesSet() throws Exception {

        final var resolver = new PathMatchingResourcePatternResolver();
        final var markdownFiles = resolver.getResources("*.md");

        for (var file : markdownFiles) {
            final var reader = new MarkdownDocumentReader(file, config);
            final var documents = reader.read();
            vectorStore.add(documents);
        }
    }
}