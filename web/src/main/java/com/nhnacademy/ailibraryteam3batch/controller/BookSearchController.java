package com.nhnacademy.ailibraryteam3batch.controller;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchRequest;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.dto.search.PageResponse;
import com.nhnacademy.ailibraryteam3batch.service.book.BookSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookSearchController {
    private final BookSearchService bookSearchService;

    @GetMapping("/")
    public String search(@ModelAttribute BookSearchRequest request,
                         @PageableDefault Pageable pageable,
                         Model model) {
        long start = System.currentTimeMillis();
        log.info("[BookSearchController] 검색 요청 Type : {}", request.searchType() == null ? "All" : request.searchType());

        Page<BookSearchResponse> result = bookSearchService.search(pageable, request);

        long searchTime = System.currentTimeMillis() - start; // ms
        log.info("검색 시간: {}초", searchTime / 1000);
        model.addAttribute("books", result.getContent());
        model.addAttribute("page", result);
        model.addAttribute("request", request);
        model.addAttribute("searchTime", searchTime / 1000.0);
        model.addAttribute("searchType", request.searchType());

        return "index";
    }

}
