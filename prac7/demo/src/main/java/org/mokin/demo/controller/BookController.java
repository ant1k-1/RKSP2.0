package org.mokin.demo.controller;

import lombok.RequiredArgsConstructor;
import org.mokin.demo.domain.Book;
import org.mokin.demo.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@RestController
public class BookController {
    private final BookService bookService;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Book>> getBookById(@PathVariable Long id) {

    }
}
