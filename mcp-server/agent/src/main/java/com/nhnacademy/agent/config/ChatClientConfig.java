package com.nhnacademy.agent.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {
    @Bean
    public ChatClient ollamaChatClient(@Qualifier(value = "ollamaChatModel") ChatModel ollamaModel,
                                       SyncMcpToolCallbackProvider mcpToolProvider) {
        return ChatClient.builder(ollamaModel)
                .defaultToolCallbacks(mcpToolProvider)
                .defaultSystem("당신은 외부 MCP 도구를 활용하는 AI입니다")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @Bean
    public ChatClient geminiChatClient(@Qualifier(value = "googleGenAiChatModel") ChatModel geminiModel,
                                       SyncMcpToolCallbackProvider mcpToolProvider) {
        return ChatClient.builder(geminiModel)
                .defaultToolCallbacks(mcpToolProvider)
                .defaultSystem("당신은 외부 MCP 도구를 활용하는 AI입니다")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    // 파라미터 추출용
    @Bean(name = "messageParseChatClient")
    public ChatClient messageParseChatClient(@Qualifier(value = "googleGenAiChatModel") ChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }
}
