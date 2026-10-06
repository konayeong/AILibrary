package com.nhnacademy.librarytelegram.bot;

import com.nhnacademy.librarytelegram.config.TelegramBotProperties;
import com.nhnacademy.librarytelegram.dto.ChatRequest;
import com.nhnacademy.librarytelegram.dto.ChatResponse;
import com.nhnacademy.librarytelegram.dto.ResultType;
import com.nhnacademy.librarytelegram.formatter.TelegramFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

/**
 * 실제 메시지 처리
 */
@Slf4j
@Component
public class LibraryTelegramBot extends TelegramLongPollingBot {
    private final RestClient restClient;
    private final TelegramBotProperties properties;
    private final TelegramFormatter formatter;

    public LibraryTelegramBot(TelegramBotProperties properties, RestClient restClient, TelegramFormatter formatter) {
        super(properties.getToken());
        this.properties = properties;
        this.restClient = restClient;
        this.formatter = formatter;
    }

    @Override
    public void onUpdateReceived(Update update) { // 새 메시지 도착 시 자동 호출
        if(update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            String response;

            log.info("[Telegram] 메시지 도착 : {} from {}", text, chatId);

            if(text.startsWith("/")) {
                response = handleCommand(text);
            }else {
                sendMessage(chatId, "검색 중 입니다. 잠시만 기다려주세요.");
                response = handleSearch(text);
            }
            sendMessage(chatId, response);
        }
    }

    @Override
    public String getBotUsername() {
        return properties.getUsername();
    }

    // 기본 명렁어 처리
    private String handleCommand(String command) {
        // TODO-R 나중에 문구 정리
        switch (command) {
            case "/start":
                return """
                    안녕하세요 지능형 도서관 도우미입니다.
                    검색어를 입력해주세요.
                    """;

            case "/help":
                return """
                    /start : 봇 설명
                    /help : 사용방법
                    자연어 : ...
                    """;
            default:
                return "존재하지 않는 명령어입니다. /start, /help 가능";
        }
    }

    // 검색 처리
    private String handleSearch(String keyword) {
        log.info("[Telegram] 도서 검색 호출 : {}", keyword);
        try {
            ChatRequest request = new ChatRequest(keyword);
            ChatResponse response = restClient.post()
                    .uri("/agent/chat")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(ChatResponse.class);

            log.info("{}", response);
            return formatter.format(response);
        } catch (Exception e) {
            log.error("Agent 호출 실패", e);
            return "검색 중 오류 발생";
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("메시지 전송 실패", e);
        }
    }
}
