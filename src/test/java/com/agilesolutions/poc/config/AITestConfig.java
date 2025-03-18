package com.agilesolutions.poc.config;

import com.pgvector.PGvector;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mistralai.MistralAiEmbeddingModel;
import org.springframework.ai.mistralai.api.MistralAiApi;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.management.ModelManagementOptions;
import org.springframework.ai.ollama.management.PullModelStrategy;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.ai.vectorstore.pinecone.PineconeVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class AITestConfig {

    @Value("${spring.ai.openai.api-key:demo}")
    String openAiKey;

    @Value("${spring.ai.vectorstore.pinecone.apiKey}")
    String pineconeApiKey;

    @Value("${spring.ai.vectorstore.pinecone.index-name}")
    String pineconeIndexName;

    @Value("${spring.ai.vectorstore.mistralai.api-key}")
    String mistralApiKey;

    @Bean
    public OpenAiApi openAiApi() {
        return new OpenAiApi.Builder().apiKey(openAiKey).build();
    }

    @Bean
    public MistralAiApi mistralAiApi() {
        return new MistralAiApi(mistralApiKey);
    }

    @Bean("ollamaChatModel")
    ChatModel chatModel() {
        OllamaApi ollamaApi = new OllamaApi("http://localhost:11434");
        return OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(OllamaOptions.builder()
                        .model(OllamaModel.LLAMA2)
                        .temperature(0.0)
                        .build())
                .modelManagementOptions(ModelManagementOptions.builder()
                        .pullModelStrategy(PullModelStrategy.WHEN_MISSING)
                        .build())
                        .build();
    }


    @Bean("openAiChatClient")
    public ChatClient openAiChatClient(OpenAiApi openAiApi, ChatMemory chatMemory, @Qualifier("openAIVectorStore") VectorStore vectorStore) {
        return ChatClient.builder(OpenAiChatModel.builder().openAiApi(openAiApi).build())
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory),
                        new QuestionAnswerAdvisor(vectorStore))
                .build();
    }

    @Bean("openAIVectorStore")
    public VectorStore openAIVectorStore(OpenAiApi openAiApi) {
         return SimpleVectorStore.builder(new OpenAiEmbeddingModel(openAiApi)).build();
    }

    @Bean("ollamaVectorStore")
    public VectorStore ollamaVectorStore() {
        return SimpleVectorStore.builder(OllamaEmbeddingModel.builder().ollamaApi(new OllamaApi("http://localhost:11434")).build()).build();
    }

    @Bean("pineconeVectorStore")
    public VectorStore pineconeVectorStore(MistralAiApi mistralAiApi) {
        return PineconeVectorStore.builder(new MistralAiEmbeddingModel(mistralAiApi))
                .apiKey(pineconeApiKey)
                .indexName(pineconeIndexName)
                .build();
    }

    @Bean("pgVectorStore")
    public PgVectorStore pgVectorStore(JdbcTemplate jdbcTemplate, OpenAiApi openAiApi) {
        return PgVectorStore.builder(jdbcTemplate, new OpenAiEmbeddingModel(openAiApi))
                .indexType(PgVectorStore.PgIndexType.HNSW)
                .distanceType(PgVectorStore.PgDistanceType.COSINE_DISTANCE)
                .dimensions(1536)
                .build();
    }

    @Bean
    public DataSource dataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate()
    {
        JdbcTemplate jdbcTemplate
                = new JdbcTemplate(dataSource());
        return jdbcTemplate;
    }

    @Bean
    public DataSourceTransactionManager txnManager()
    {
        DataSourceTransactionManager txnManager
                = new DataSourceTransactionManager(dataSource());
        return txnManager;
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
