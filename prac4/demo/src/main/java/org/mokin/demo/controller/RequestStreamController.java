package org.mokin.demo.controller;

import org.mokin.demo.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/books")
public class RequestStreamController {
    private final RSocketRequester rSocketRequester;

    @Autowired
    public RequestStreamController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @GetMapping
    public Flux<Book> getBooks() {
        return rSocketRequester
            .route("getBooks")
            .data(new Book())
            .retrieveFlux(Book.class);
    }
}
