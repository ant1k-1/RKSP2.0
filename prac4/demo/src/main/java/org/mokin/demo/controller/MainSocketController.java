package org.mokin.demo.controller;

import org.mokin.demo.domain.Book;
import org.mokin.demo.repo.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class MainSocketController {
    private final BookRepo bookRepository;

    @Autowired
    public MainSocketController(BookRepo bookRepository) {
        this.bookRepository = bookRepository;
    }

    @MessageMapping("getBook")
    public Mono<Book> getBook(Long id) {
        return Mono.justOrEmpty(bookRepository.findById(id));
    }

    @MessageMapping("addBook")
    public Mono<Book> addBook(Book hat) {
        return Mono.justOrEmpty(bookRepository.save(hat));
    }

    @MessageMapping("getBooks")
    public Flux<Book> getBooks() {
        return Flux.fromIterable(bookRepository.findAll());
    }

    @MessageMapping("deleteBook")
    public Mono<Void> deleteBook(Long id) {
        Book book = bookRepository.findById(id).get();
        bookRepository.delete(book);
        return Mono.empty();
    }

    @MessageMapping("bookChannel")
    public Flux<Book> bookChannel(Flux<Book> books) {
        return books.flatMap(book -> Mono.fromCallable(() ->
                bookRepository.save(book)))
            .collectList()
            .flatMapMany(savedBooks -> Flux.fromIterable(savedBooks));
    }
}
