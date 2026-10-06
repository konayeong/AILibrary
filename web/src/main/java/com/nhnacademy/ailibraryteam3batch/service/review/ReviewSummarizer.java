package com.nhnacademy.ailibraryteam3batch.service.review;

import com.nhnacademy.ailibraryteam3batch.domain.review.BookReview;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewSummarizer {

    private final ChatClient chatClient;

    private static final int CHUNK_SIZE = 10;

    public ReviewSummarizer(@Qualifier("reviewChatClientBuilder")ChatClient.Builder chatBuilder) {
        this.chatClient = chatBuilder.build();
    }

    public String summarizeReviews(List<BookReview> reviews) {
        // 리뷰를 CHUNK_SIZE개씩 묶기
        List<List<BookReview>> chunking = chunking(reviews);

        // 각 청크를 요약
        List<String> chunkSummarizes = new ArrayList<>();
        for(List<BookReview> chunk : chunking) {
            chunkSummarizes.add(summarizeChunk(chunk));
        }

        // 부분 요약본들을 합쳐서 최종 요약
        if(chunkSummarizes.size() == 1) {
            return chunkSummarizes.getFirst();
        }

        StringBuilder builder = new StringBuilder();
        for(String review : chunkSummarizes) {
            builder.append("-").append(review).append("\n");
        }

        return callAI(builder.toString());
    }

    public String summarizeIncremental(String summary, List<BookReview> reviews) {
        // 새 리뷰 요약
        String newSummary = summarizeReviews(reviews);

        // 부분 요약본 + 기존 요약을 머지
        String prompt = String.format("""
            너는 도서 리뷰 요약본을 전문적으로 통합하고 정제하는 봇이다.
            제공된 [기존 요약]의 핵심 맥락을 완벽히 유지하면서, [새로운 리뷰 요약본]에서 추가된 정보(새로운 장점, 단점, 의견)를 자연스럽게 결합하여 하나의 완성된 '최종 요약'을 작성해라.
        
            [반드시 지켜야 할 제약 조건]
            1. 인사말, 서론, 결론("네, 요약해 드리겠습니다", "다음은 요약본입니다" 등)은 절대 출력하지 마라.
            2. 제목 마크다운(### 장점, **단점** 등)이나 백틱(```) 기호는 절대 사용하지 말고, 오직 제시된 [출력 형식]의 텍스트 포맷만 그대로 채워라.
            3. '장점'과 '단점'은 중복되는 내용을 통합하여 각각 2~3개의 명확한 문장으로 작성하고, 반드시 글머리 기호('- ')를 붙여라.
            4. '추천 대상'은 리뷰어들이 공통적으로 말하는 타겟 독자층을 아우르는 깔끔한 줄글 형태로 1~2문장으로 작성해라.
        
            [기존 요약]
            %s
        
            [새로운 리뷰 요약본]
            %s
        
            [출력 형식]
            장점:
            -
            -
            단점:
            -
            -
            총평:
            -
            추천 대상:
            -
        """, summary, newSummary);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    private List<List<BookReview>> chunking(List<BookReview> reviews) {
        List<List<BookReview>> chunking = new ArrayList<>();
        for (int i=0; i<reviews.size(); i+=CHUNK_SIZE) {
            chunking.add(new ArrayList<>(reviews.subList(i, Math.min(i+ CHUNK_SIZE, reviews.size()))));
        }

        return chunking;
    }

    private String summarizeChunk(List<BookReview> chunk) {
        StringBuilder builder = new StringBuilder();
        for(BookReview review : chunk) {
            builder.append("-").append(review.getContent()).append("\n");
        }

        return callAI(builder.toString());
    }

    private String callAI(String input) {
        String prompt = """
                너는 수백 개의 도서 리뷰를 분석하여 핵심만 추출하는 '도서 리뷰 요약 전문가'이다.
                제공된 리뷰 목록을 바탕으로 [출력 형식]에 맞춰 최종 요약본을 작성해라.
            
                [반드시 지켜야 할 제약 조건]
                1. 인사말, 서론, 결론("네, 요약해 드리겠습니다", "다음은 요약본입니다" 등)은 절대 출력하지 마라.
                2. 제목 마크다운(### 장점, **단점** 등)이나 백틱(```) 기호는 절대 사용하지 말고, 오직 제시된 [출력 형식]의 텍스트 포맷만 그대로 채워라.
                3. '장점'과 '단점'은 중복되는 내용을 통합하여 각각 1~5개의 명확한 문장으로 작성하고, 반드시 글머리 기호('- ')를 붙여라.
                4. '추천 대상'은 리뷰어들이 공통적으로 말하는 타겟 독자층을 아우르는 깔끔한 줄글 형태로 1~2문장으로 작성해라.
            
                [출력 형식]
                장점:
                -
                -
                -
                단점:
                -
                -
                -
                총평:
                -
                추천 대상:
                -
            """;

        String userPrompt = String.format("""
                ### 분석할 리뷰 데이터 ###
                %s
            """, input);

        return chatClient.prompt()
                .system(prompt)
                .user(userPrompt)
                .call()
                .content();
    }
}
