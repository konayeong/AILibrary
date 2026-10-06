package com.nhnacademy.ailibraryteam3batch.dto.cache;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CacheResult {
    private String keyword;

    private CacheStatus status;

    private List<BookSearchResponse> books;
}
