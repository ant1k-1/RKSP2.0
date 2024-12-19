package org.mokin.demo.controller;

import lombok.RequiredArgsConstructor;
import org.mokin.demo.domain.Book;
import org.mokin.demo.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@RestController
public class BookController {
    private final BookService bookService;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Book>> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<ResponseEntity<Book>> getBooks(
        @RequestParam(name = "yearFrom", required = false) Integer yearFrom,
        @RequestParam(name = "yearTo", required = false) Integer yearTo
    ) {
        return bookService.getAllBooks(yearFrom, yearTo)
            .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<Book>> createBook(@RequestBody Book book) {
        return bookService.createBook(book).map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Book>> updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBook(id, book).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable Long id) {
        return bookService.deleteBook(id).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
