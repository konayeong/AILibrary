package com.nhnacademy.ailibraryteam3batch.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id) {
        super(String.format("ID : %d에 해당하는 도서를 찾을 수 없습니다.", id));
    }
}
