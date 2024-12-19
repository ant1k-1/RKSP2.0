package org.mokin.demo;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mokin.demo.domain.Book;
import org.mokin.demo.repository.BookRepository;
import org.mokin.demo.service.BookService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    public BookServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBookById_shouldReturnBook() {
        Long bookId = 1L;
        Book book = new Book(bookId, "Title", "Author", "ISBN", "Publisher", 2023);

        when(bookRepository.findById(bookId)).thenReturn(Mono.just(book));

        StepVerifier.create(bookService.getBookById(bookId))
            .expectNext(book)
            .verifyComplete();

        verify(bookRepository).findById(bookId);
    }

    @Test
    void getAllBooks_shouldReturnFilteredBooks() {
        Book book1 = new Book(1L, "Title1", "Author1", "ISBN1", "Publisher1", 2020);
        Book book2 = new Book(2L, "Title2", "Author2", "ISBN2", "Publisher2", 2023);

        when(bookRepository.findAll()).thenReturn(Flux.just(book1, book2));

        StepVerifier.create(bookService.getAllBooks(2021, null))
            .expectNextMatches(book -> book.getTitle().equals("TITLE2"))
            .verifyComplete();

        verify(bookRepository).findAll();
    }

    @Test
    void createBook_shouldSaveAndReturnBook() {
        Book book = new Book(null, "New Title", "New Author", "New ISBN", "New Publisher", 2023);

        when(bookRepository.save(book)).thenReturn(Mono.just(book));

        StepVerifier.create(bookService.createBook(book))
            .expectNext(book)
            .verifyComplete();

        verify(bookRepository).save(book);
    }

    @Test
    void updateBook_shouldUpdateAndReturnBook() {
        Long bookId = 1L;
        Book existingBook = new Book(bookId, "Old Title", "Old Author", "Old ISBN", "Old Publisher", 2020);
        Book updatedBook = new Book(bookId, "New Title", "New Author", "New ISBN", "New Publisher", 2023);

        when(bookRepository.findById(bookId)).thenReturn(Mono.just(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(updatedBook));

        StepVerifier.create(bookService.updateBook(bookId, updatedBook))
            .expectNext(updatedBook)
            .verifyComplete();

        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void deleteBook_shouldDeleteBook() {
        Long bookId = 1L;
        Book book = new Book(bookId, "Title", "Author", "ISBN", "Publisher", 2023);

        when(bookRepository.findById(bookId)).thenReturn(Mono.just(book));
        when(bookRepository.delete(book)).thenReturn(Mono.empty());

        StepVerifier.create(bookService.deleteBook(bookId))
            .verifyComplete();

        verify(bookRepository).findById(bookId);
        verify(bookRepository).delete(book);
    }
}

