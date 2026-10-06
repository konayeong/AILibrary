package com.nhnacademy.ailibraryteam3batch.controller;

import com.nhnacademy.ailibraryteam3batch.dto.book.BookDetailResponse;
import com.nhnacademy.ailibraryteam3batch.dto.review.BookReviewDto;
import com.nhnacademy.ailibraryteam3batch.dto.review.ReviewCreateRequest;
import com.nhnacademy.ailibraryteam3batch.dto.review.ReviewStatsDto;
import com.nhnacademy.ailibraryteam3batch.service.book.BookService;
import com.nhnacademy.ailibraryteam3batch.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class BookDetailController {
    private final BookService bookService;
    private final ReviewService reviewService;

    @GetMapping("/books/{id}")
    public String getBookDetail(@PathVariable Long id,
                                @PageableDefault Pageable pageable,
                                Model model) {
        BookDetailResponse book = bookService.getBookById(id);
        model.addAttribute("book", book);

        ReviewStatsDto bookSummary = reviewService.getReviewSummary(id);
        model.addAttribute("bookSummary", bookSummary);

        String aiSummary = reviewService.getAiSummaryMessage(id);
        model.addAttribute("aiSummary", aiSummary);

        Page<BookReviewDto> bookReviews = reviewService.getReviewList(id, pageable);

        model.addAttribute("reviews", bookReviews.getContent());
        model.addAttribute("page", bookReviews);

        model.addAttribute("bookId", id);

        return "detail";
    }

    @PostMapping("/books/{id}/reviews")
    public String addReviews(@PathVariable Long id,
                             @ModelAttribute ReviewCreateRequest request) {
        reviewService.createReview(id, request);
        return "redirect:/books/" + id;
    }
}
