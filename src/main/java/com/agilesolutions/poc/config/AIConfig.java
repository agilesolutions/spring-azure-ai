package com.agilesolutions.poc.config;

import com.azure.ai.openai.OpenAIClientBuilder;
import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    private static final String DEFAULT_DEPLOYMENT_NAME = "gpt-4o";

    private static final Double DEFAULT_TEMPERATURE = 0.7;

    @Bean("openAiChatModel")
    ChatModel aiClient(OpenAIClientBuilder openAIClientBuilder) {

        return new AzureOpenAiChatModel(openAIClientBuilder);

    }

    @Bean
    public OpenAIClientBuilderCustomizer responseTimeoutCustomizer() {
        return openAiClientBuilder -> {
            HttpClientOptions clientOptions = new HttpClientOptions()
                    .setResponseTimeout(Duration.ofMinutes(5));
            openAiClientBuilder.httpClient(HttpClient.createDefault(clientOptions));
        };
    }

}
