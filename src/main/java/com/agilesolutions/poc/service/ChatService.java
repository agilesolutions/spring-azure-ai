package com.agilesolutions.poc.service;

import com.agilesolutions.poc.model.Bom;
import com.microsoft.applicationinsights.TelemetryClient;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class ChatService {

    @Qualifier("openAiChatModel")
    private final ChatClient aiClient;

    @Qualifier("simpleVectorStore")
    private final VectorStore vectorStore;

    private final TelemetryClient telemetryClient;

    public String getBom(String version) {

        BeanOutputConverter<String> outputConverter = new BeanOutputConverter<>(String.class);

        String promptString = "list only library name and version for all java spring library version contained by springboot with version {version}{format}";

        PromptTemplate promptTemplate = new PromptTemplate(promptString);
        promptTemplate.add("version", version);
        promptTemplate.add("format", outputConverter.getFormat());

        telemetryClient.trackEvent("UserMessage", Map.of("message", promptString), null);

        ChatResponse response = aiClient.call(promptTemplate.create());

        telemetryClient.trackEvent(response.getResult().getOutput().getContent());

        return outputConverter.convert(response.getResult().getOutput().getContent());

    }

    public Bom getBomModel(String version) {

        return aiClient.prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults())=
                .user()
    }
}