package com.nhnacademy.librarytelegram.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 설정값 관리
 */
@Component
@Getter @Setter
@ConfigurationProperties(prefix = "telegram.bot")
public class TelegramBotProperties {
    private boolean enabled;
    private String token;
    private String username;
}
