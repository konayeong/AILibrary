package com.nhnacademy.ailibraryteam3batch.service.book;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookRagService {

    List<BookSearchResponse> ragSearch(String question, List<BookSearchResponse> hybridResult);
}
