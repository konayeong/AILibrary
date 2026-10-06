package com.nhnacademy.ailibraryteam3batch.service.book.strategy;

import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BookSearchStrategyFactory {

    private final Map<SearchType, BookSearchStrategy> strategyMap;

    public BookSearchStrategyFactory(List<BookSearchStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        BookSearchStrategy::getSearchType,
                        s -> s
                ));
    }

    public BookSearchStrategy get(SearchType type) {
        return strategyMap.get(type);
    }
}
