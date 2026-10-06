package com.nhnacademy.ailibraryteam3batch;

import org.springframework.ai.model.google.genai.autoconfigure.chat.GoogleGenAiChatAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude = { GoogleGenAiChatAutoConfiguration.class }) // 💡 자동 설정 제외
public class AiLibraryTeam3BatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiLibraryTeam3BatchApplication.class, args);
    }

}
