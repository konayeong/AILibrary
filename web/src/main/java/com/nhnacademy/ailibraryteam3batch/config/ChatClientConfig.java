package com.nhnacademy.ailibraryteam3batch.config;

import com.google.genai.Client;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ChatClientConfig {
    @Bean
    @Primary
    public ChatClient.Builder ollamaChatClientBuilder(
            @Qualifier("ollamaChatModel") ChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel);
    }

    @Bean
    public ChatClient.Builder geminiChatClientBuilder (
            @Value("${ai.rag.api-key}") String apiKey,
            @Value("${ai.rag.model}") String model,
            @Value("${ai.rag.temperature}") Double temperature,
            @Value("${ai.rag.top-k}") Integer topK) {

        // 1. RAG API 키를 주입하여 독립된 구글 GenAI 오피셜 클라이언트를 빌드합니다.
        Client genAiClient = Client.builder()
                .apiKey(apiKey)
                .build();

        // 2. 주입받은 전용 클라이언트를 얹어서 독립된 ChatModel 인스턴스를 생성합니다.
        GoogleGenAiChatModel ragChatModel = GoogleGenAiChatModel.builder()
                .genAiClient(genAiClient)
                .build();

        GoogleGenAiChatOptions options = GoogleGenAiChatOptions.builder()
                .model(model)
                .temperature(temperature)
                .topK(topK)
                .build();

        return ChatClient.builder(ragChatModel)
                .defaultOptions(options);
    }

    @Bean
    public ChatClient.Builder reviewChatClientBuilder(@Value("${ai.review.api-key}") String apiKey,
                                                      @Value("${ai.review.model}") String model,
                                                      @Value("${ai.review.temperature}") Double temperature,
                                                      @Value("${ai.review.max-output-tokens}") Integer maxTokens,
                                                      @Value("${ai.review.top-p}") Double topP,
                                                      @Value("${ai.review.top-k}") Integer topK) {

        // 1. Review API 키를 주입하여 완전히 격리된 별도의 구글 클라이언트를 빌드합니다.
        Client genAiClient = Client.builder()
                .apiKey(apiKey)
                .build();

        // 2. 리뷰 전용 클라이언트를 얹어 또 하나의 독립된 ChatModel을 생성합니다.
        GoogleGenAiChatModel reviewChatModel = GoogleGenAiChatModel.builder()
                .genAiClient(genAiClient)
                .build();

        GoogleGenAiChatOptions options = GoogleGenAiChatOptions.builder()
                .model(model)
                .temperature(temperature)
                .maxOutputTokens(maxTokens)
                .topP(topP)
                .topK(topK)
                .build();

        return ChatClient.builder(reviewChatModel)
                .defaultOptions(options)
                .defaultAdvisors(new SimpleLoggerAdvisor());
    }
}
