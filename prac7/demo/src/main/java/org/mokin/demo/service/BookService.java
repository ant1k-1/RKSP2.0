package org.mokin.demo.service;

import lombok.RequiredArgsConstructor;
import org.mokin.demo.domain.Book;
import org.mokin.demo.repository.BookRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;

    public Mono<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Flux<Book> getAllBooks(Integer yearFrom, Integer yearTo) {
        return yearFrom != null || yearTo != null ? bookRepository.findAll()
            .filter(book -> yearFrom == null || (book.getYear() >= yearFrom))
            .filter(book -> yearTo == null || (book.getYear() <= yearTo))
            .map()
    }
}
