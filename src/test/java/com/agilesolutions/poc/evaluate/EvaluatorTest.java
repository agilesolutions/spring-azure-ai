package com.agilesolutions.poc.evaluate;

import com.agilesolutions.poc.config.AITestConfig;
import com.agilesolutions.poc.loader.StockLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.evaluation.FactCheckingEvaluator;
import org.springframework.ai.evaluation.RelevancyEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = {AITestConfig.class, StockLoader.class}, initializers = {ConfigDataApplicationContextInitializer.class})
@TestPropertySource(properties = { "spring.config.location=classpath:application.yaml" })
public class EvaluatorTest {

    @Autowired
    ChatClient contentGenerator;

    @Autowired
    RelevancyEvaluator relevancyEvaluator;

    @Autowired
    FactCheckingEvaluator factCheckingEvaluator;

    @ParameterizedTest
    @ValueSource(strings = {"Find the most growth trends"})
    void whenSearchingTrends_thenRelevantStocksReturned(String question) {

        ChatResponse chatResponse = contentGenerator.prompt()
                .user(question)
                .call()
                .chatResponse();

        String answer = chatResponse.getResult().getOutput().toString();
        List<Document> documents = chatResponse.getMetadata().get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS);
        EvaluationRequest evaluationRequest = new EvaluationRequest(question, documents, answer);

        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);
        assertThat(evaluationResponse.isPass()).isTrue();

        String nonRelevantAnswer  = "The stock with the most percentage growth is **NVDA**, with approximately **99.00% growth";
        evaluationRequest = new EvaluationRequest(question, documents, nonRelevantAnswer);
        evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);
        assertThat(evaluationResponse.isPass()).isFalse();

    }

    @ParameterizedTest
    @ValueSource(strings = {"Find the most growth trends"})
    void whenSearchingTrends_thenFactualStocksReturned(String question) {

        ChatResponse chatResponse = contentGenerator.prompt()
                .user(question)
                .call()
                .chatResponse();

        String answer = chatResponse.getResult().getOutput().toString();
        List<Document> documents = chatResponse.getMetadata().get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS);
        EvaluationRequest evaluationRequest = new EvaluationRequest(question, documents, answer);

        EvaluationResponse evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);
        assertThat(evaluationResponse.isPass()).isTrue();

        String wrongAnswer = "The stock with the most percentage growth is **NVDA**, with approximately **99.00% growth";
        evaluationRequest = new EvaluationRequest(question, documents, wrongAnswer);
        evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);
        assertThat(evaluationResponse.isPass()).isFalse();

    }
}
