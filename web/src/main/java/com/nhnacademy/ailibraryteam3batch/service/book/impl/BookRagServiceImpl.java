package com.nhnacademy.ailibraryteam3batch.service.book.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ailibraryteam3batch.client.RerankClient;
import com.nhnacademy.ailibraryteam3batch.domain.review.BookReviewSummary;
import com.nhnacademy.ailibraryteam3batch.dto.rag.*;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.repository.review.BookReviewSummaryRepository;
import com.nhnacademy.ailibraryteam3batch.service.book.BookRagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookRagServiceImpl implements BookRagService {

    private final ChatClient chatClient;
    private final RerankClient rerankClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final BookReviewSummaryRepository summaryRepository;

    public BookRagServiceImpl(
            @Qualifier("ollamaChatClientBuilder") ChatClient.Builder ollamaChatClientBuilder,
            @Qualifier("geminiChatClientBuilder") ChatClient.Builder geminiChatClientBuilder,
            RerankClient rerankClient,
            @Value("${spring.ai.selected-model}") String selectedModel,
            BookReviewSummaryRepository summaryRepository) {

        if (selectedModel.equalsIgnoreCase("gemini")) {
            log.info("AI: gemini");
            this.chatClient = geminiChatClientBuilder.build();
        } else {
            log.info("AI: ollama");
            this.chatClient = ollamaChatClientBuilder.build();
        }

        this.rerankClient = rerankClient;
        this.summaryRepository = summaryRepository;
    }

    //BookSearchResponse 에 content 추가, @Setter
    @Override
    public List<BookSearchResponse> ragSearch(String question, List<BookSearchResponse> hybridResult) {

        //hybrid 검색 결과
        List<BookSearchResponse> books = hybridResult.stream()
                .limit(10)
                .toList();
        log.info("검색된 도서 수: {}", books.size());

        //context 생성
        String context = buildContext(books);

        //prompt 생성
        String prompt = buildPrompt(question, context);

        long llmStart = System.currentTimeMillis();
        //llm 호출
        ChatResponse response = chatClient
                .prompt(prompt)
                .call()
                .chatResponse();
        long llmEnd = System.currentTimeMillis();
        log.info("LLM Search 시간: {}ms ({}초)",
                llmEnd - llmStart,
                (llmEnd - llmStart) / 1000.0);

        String json = response.getResult().getOutput().getText();

        Usage usage = response.getMetadata().getUsage();

        log.info("Prompt Tokens : {}", usage.getPromptTokens());
        log.info("Completion Tokens : {}", usage.getCompletionTokens());
        log.info("Total Tokens : {}", usage.getTotalTokens());

        //List<BookRecommendAiResponse>로 변환
        List<BookRagAiResponse> recommendResponseList;
        try {
            recommendResponseList =
                    objectMapper.readValue(json, new TypeReference<List<BookRagAiResponse>>() {
                    });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //관련성 순으로 결과 정렬
        List<BookSearchResponse> result = new ArrayList<>();

        for (BookRagAiResponse recommendBook : recommendResponseList) {
            BookSearchResponse bookResponse =
                    books.stream()
                            .filter(book ->
                                    book.getIsbn().equals(recommendBook.isbn()))
                            .findFirst()
                            .orElse(null);

            if (bookResponse == null) {
                continue;
            }

            result.add(new BookSearchResponse(
                    bookResponse.getId(),
                    recommendBook.isbn(),
                    bookResponse.getTitle(),
                    bookResponse.getAuthor(),
                    bookResponse.getPublisherName(),
                    bookResponse.getPrice(),
                    bookResponse.getImageUrl(),
                    recommendBook.reason(),
                    bookResponse.getVolumeName(),
                    bookResponse.getSimilarity(),
                    bookResponse.getRrfScore(),
                    recommendBook.relevance()
            ));
        }
        return result;
    }

    // book내용을 prompt에 넣을 수 있게 변환
    private String buildContext(List<BookSearchResponse> books) {
        StringBuilder sb = new StringBuilder();

        List<Long> ids = books.stream()
                .map(BookSearchResponse::getId)
                .toList();

        Map<Long, BookReviewSummary> summaryMap =
                summaryRepository.findAllById(ids)
                        .stream()
                        .collect(Collectors.toMap(
                                BookReviewSummary::getId,
                                summary -> summary
                        ));

        sb.append("도서 정보\n");
        for (BookSearchResponse book : books) {
            BookReviewSummary summary = summaryMap.get(book.getId());
            //리뷰 요약
            String reviewSummary = summary != null
                            ? summary.getReviewSummary()
                            : "리뷰 없음";
            //별점 평균
            BigDecimal averageRating = summary != null
                                        ? summary.getAverageRating() : BigDecimal.ZERO;

            sb.append("제목: ").append(book.getTitle()).append("\n")
                    .append("내용: ").append(book.getContent()).append("\n")
                    .append("유사도: ").append(book.getSimilarity()).append("\n")
                    .append("리뷰요약: ").append(reviewSummary).append("\n")
                    .append("별점평군: ").append(averageRating).append("\n")
                    .append("isbn: ").append(book.getIsbn()).append("\n")
                    .append("\n");
        }

        return sb.toString();
    }

    // question, context 기반 prompt 생성
    private String buildPrompt(String question, String context) {
        return String.format("""
                        당신은 경험 많은 도서관 사서입니다.
                        사용자의 질문에 친절하고 상세하게 답변해주세요.
                        
                        ## 질문
                        %s
                        
                        ## 참고 도서 정보
                        %s
                        
                        ## 답변 형식
                        [
                          {
                            "isbn": isbn 번호,
                            "reason": 추천 사유,
                            "relevance": 관련성
                          }
                        ]
                        
                        ## 답변 규칙
                        - 반드시 참고 도서 정보를 바탕으로 답변하세요.
                        - 각 도서마다 왜 추천하는지 명확한 이유를 제시하세요.
                        - 추천 사유는 content, 리뷰총평 기반으로 한줄로 작성하세요.
                        - 최대 5권까지 추천해주세요.
                        - 추천 사유를 한 문장으로 작성하세요.
                        - isbn 번호는 반드시 참고 도서에 있는 번호 사용하세요.
                        - 관련성은 유사도값, 추천 사유, 리뷰 요약을 기반으로 100점 만점으로 작성하세요.
                        - JSON 형식으로 답변해주세요.
                        - 순서를 변경하지 마세요.
                        - Markdown을 사용하지 마세요.
                        
                        """,
                question, context);
    }

}
