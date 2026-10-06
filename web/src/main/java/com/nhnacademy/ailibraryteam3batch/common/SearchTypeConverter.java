package com.nhnacademy.ailibraryteam3batch.common;

import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SearchTypeConverter implements Converter<String, SearchType> {
    @Override
    public SearchType convert(String source) {
        return SearchType.valueOf(source.toUpperCase());
    }
}
