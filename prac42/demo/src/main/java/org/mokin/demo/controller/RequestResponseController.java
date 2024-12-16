package org.mokin.demo.controller;

import org.mokin.demo.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/books")
public class RequestResponseController {
    private final RSocketRequester rSocketRequester;

    @Autowired
    public RequestResponseController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @GetMapping("/{id}")
    public Mono<Book> getBook(@PathVariable Long id) {
        return rSocketRequester
            .route("getBook")
            .data(id)
            .retrieveMono(Book.class);
    }

    @PostMapping
    public Mono<Book> addBook(@RequestBody Book hat) {
        return rSocketRequester
            .route("addBook")
            .data(hat)
            .retrieveMono(Book.class);
    }
}
