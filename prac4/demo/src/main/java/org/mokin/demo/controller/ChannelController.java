package org.mokin.demo.controller;

import org.mokin.demo.domain.Book;
import org.mokin.demo.dto.BookListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class ChannelController {
    private final RSocketRequester rSocketRequester;

    @Autowired
    public ChannelController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @PostMapping("/exp")
    public Flux<Book> addBooksMultiple(@RequestBody BookListDto bookListDto){
        List<Book> bookList = bookListDto.getBooks();
        Flux<Book> books = Flux.fromIterable(bookList);
        return rSocketRequester
            .route("bookChannel")
            .data(books)
            .retrieveFlux(Book.class);
    }
}
