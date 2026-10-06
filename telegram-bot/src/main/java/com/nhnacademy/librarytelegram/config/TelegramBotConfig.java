package com.nhnacademy.librarytelegram.config;

import com.nhnacademy.librarytelegram.bot.LibraryTelegramBot;
import com.nhnacademy.librarytelegram.formatter.TelegramFormatter;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.web.client.RestClient;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Bot 설정
 * Telegram Bots API 초기화
 * Bot 등록 및 생명주기 관리
 * 의존성 주입 (service)
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "telegram.bot.enabled", havingValue = "true")
public class TelegramBotConfig {
    private final TelegramBotProperties telegramBotProperties;
    private BotSession botSession; // Telegram Polling Thread를 관리하는 객체
    private final TelegramFormatter formatter;

    @Bean
    public LibraryTelegramBot libraryTelegramBot(RestClient restClient) {
        return new LibraryTelegramBot(telegramBotProperties, restClient, formatter);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startBot(ApplicationReadyEvent event) {
        LibraryTelegramBot bot = event.getApplicationContext().getBean(LibraryTelegramBot.class);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botSession = botsApi.registerBot(bot); // 내부 스레드 시작
            log.info("[Telegram] Bot 등록 성공 : @{}", bot.getBotUsername());
        }catch (TelegramApiException e) {
            log.error("[Telegram] Bot 등록 실패", e);
        }
    }

    @PreDestroy
    public void stopBot() {
        if(botSession != null && botSession.isRunning()) {
            try {
                botSession.stop();
                log.info("[Telegram] Bot 스레드 안전 종료");
            }catch (Exception e) {
                log.error("[Telegram] Bot 스레드 종료 실패", e);
            }
        }
    }
}
