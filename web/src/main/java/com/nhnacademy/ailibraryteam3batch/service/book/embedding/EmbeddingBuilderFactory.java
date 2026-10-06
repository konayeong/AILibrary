package com.nhnacademy.ailibraryteam3batch.service.book.embedding;

import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EmbeddingBuilderFactory {

    private final Map<SearchType, EmbeddingBuilder> builders;

    public EmbeddingBuilderFactory(List<EmbeddingBuilder> list) {
        this.builders = list.stream()
                .collect(Collectors.toMap(
                        EmbeddingBuilder::getSearchType,
                        Function.identity()
                ));
    }

    public EmbeddingBuilder get(SearchType type) {
        return builders.get(type);
    }
}
