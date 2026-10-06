package com.nhnacademy.agent.controller;

import com.nhnacademy.agent.dto.ChatRequest;
import com.nhnacademy.agent.dto.ChatResponse;
import com.nhnacademy.agent.dto.LibrarySearchParam;
import com.nhnacademy.agent.service.AgentService;
import com.nhnacademy.agent.service.NatureLangParseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/agent")
public class LibraryController {
    private final AgentService agentService;
    private final NatureLangParseService natureLangParseService;

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        log.info("[Agent] 텔레그램 메시지 수신 : {}", request.message());
        LibrarySearchParam parse = natureLangParseService.parse(request.message());

        return agentService.chat(parse);
    }
}
