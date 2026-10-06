package com.nhnacademy.ailibraryteam3batch.service.book.strategy;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchRequest;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import com.nhnacademy.ailibraryteam3batch.repository.book.BookRepository;
import com.nhnacademy.ailibraryteam3batch.service.book.embedding.EmbeddingBuilder;
import com.nhnacademy.ailibraryteam3batch.service.book.embedding.EmbeddingBuilderFactory;
import com.nhnacademy.ailibraryteam3batch.service.cache.RedisService;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class HybridSearchStrategy implements BookSearchStrategy{

    private final BookRepository bookRepository;
    private final EmbeddingModel embeddingModel;
    private final TaskExecutor taskExecutor;    // 비동기 작업을 실행할 스레드 풀
    private static final Integer DEFAULT_BATCH_SIZE = 100;


    public HybridSearchStrategy(@Qualifier("openAiEmbeddingModel") EmbeddingModel embeddingModel,
                                @Qualifier("applicationTaskExecutor") TaskExecutor taskExecutor,
                                BookRepository bookRepository) {
        this.embeddingModel = embeddingModel;
        this.taskExecutor = taskExecutor;
        this.bookRepository = bookRepository;
    }


    @Override
    public SearchType getSearchType() {
        return SearchType.HYBRID;
    }

    @Override
    public List<BookSearchResponse> search(BookSearchRequest request) {
        return searchByHybrid(request);
    }

    private List<BookSearchResponse> searchByHybrid(BookSearchRequest request) {
        String keyword = request.keyword();
        Map<Long, Double> rrfScores = new HashMap<>();

        Map<Long, BookSearchResponse> bookMap = new HashMap<>();

        /// supplyAsync -> 별도 스레드에서 작업 실행 후 CompletableFuture 반환

        /// 키워드 검색 (병렬로 실행)
        CompletableFuture<List<BookSearchResponse>> keywordSearchFuture = CompletableFuture.supplyAsync(() -> {
            return bookRepository.searchByKeyword(keyword).stream().toList();
        }, taskExecutor);

        /// 벡터 검색 (병렬로 실행)
        CompletableFuture<List<BookSearchResponse>> vectorSearchFuture = CompletableFuture.supplyAsync(() -> {
            float[] vector = embeddingModel.embed(keyword);
            BookSearchRequest vectorRequest = new BookSearchRequest(
                    request.keyword(),
                    request.isbn(),
                    SearchType.VECTOR,
                    vector
            );
            return bookRepository.vectorSearch(vectorRequest).stream().toList();
        }, taskExecutor);

        /// 두 검색이 모두 완료될 때 실행
        CompletableFuture<List<BookSearchResponse>> fusedResultsFuture = keywordSearchFuture.thenCombineAsync(
                vectorSearchFuture,
                (keywordResults, vectorResults) -> {
                    /// 두 결과가 모두 도착했을 때 호출

                    /// 키워드 검색으로 인한 RRF점수 계산 후 추가
                    for (int i = 0; i < keywordResults.size(); i++) {
                        Long id = keywordResults.get(i).getId();
                        rrfScores.merge(id, 1.0 / (60 + i + 1), Double::sum);
                        bookMap.putIfAbsent(id, keywordResults.get(i));
                    }

                    /// 벡터 검색으로 인한 RRF점수 계산 후 추가
                    for (int i = 0; i < vectorResults.size(); i++) {
                        Long id = vectorResults.get(i).getId();
                        rrfScores.merge(id, 1.0 / (60 + i + 1), Double::sum);
                        bookMap.putIfAbsent(id, vectorResults.get(i));
                    }
                    return rrfScores.entrySet().stream()
                            .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                            .map(entry -> {
                                BookSearchResponse book = bookMap.get(entry.getKey());
                                book.setRrfScore(entry.getValue());
                                return book;
                            })
                            .toList();
                }, taskExecutor
        );

        /// 최종 결과 가져오기 (필요시 대기(블로킹))
        List<BookSearchResponse> fusedResults = fusedResultsFuture.join();

        return fusedResults;
    }
}
