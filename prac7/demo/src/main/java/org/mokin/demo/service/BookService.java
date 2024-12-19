package org.mokin.demo.service;

import lombok.RequiredArgsConstructor;
import org.mokin.demo.domain.Book;
import org.mokin.demo.exception.BookException;
import org.mokin.demo.repository.BookRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Locale;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;

    public Mono<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Flux<Book> getAllBooks(Integer yearFrom, Integer yearTo) {
        return yearFrom != null || yearTo != null
            ? bookRepository.findAll()
            .filter(book -> yearFrom == null || (book.getYear() >= yearFrom))
            .filter(book -> yearTo == null || (book.getYear() <= yearTo))
            .map(this::transformBook)
            .onErrorResume(e -> Flux.error(new BookException("Some book fail"))) // Обработка ошибок
            .onBackpressureBuffer() // Работа в формате backpressure
            : bookRepository.findAll();
    }

    public Mono<Book> createBook(Book book) {
        return bookRepository.save(book);
    }

    public Mono<Book> updateBook(Long id, Book book) {
        return bookRepository.findById(id)
            .flatMap(existingBook -> {
                existingBook.setTitle(book.getTitle());
                existingBook.setAuthor(book.getAuthor());
                existingBook.setIsbn(book.getIsbn());
                existingBook.setPublisher(book.getPublisher());
                existingBook.setYear(book.getYear());
                return bookRepository.save(existingBook);
            });
    }

    public Mono<Void> deleteBook(Long id) {
        return bookRepository.findById(id)
            .flatMap(bookRepository::delete);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private Book transformBook(Book book) {
        // Пример преобразования объекта
        book.setTitle(book.getTitle().toUpperCase(Locale.ROOT));
        return book;
    }
}
