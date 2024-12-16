package org.mokin.demo;

import org.junit.jupiter.api.Test;
import org.mokin.demo.domain.Book;
import org.mokin.demo.repo.BookRepo;
import org.springframework.boot.test.context.SpringBootTest;

import io.rsocket.frame.decoder.PayloadDecoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

    @Autowired
    private BookRepo bookRepository;
    private RSocketRequester requester;

    @BeforeEach
    public void setup() {
        requester = RSocketRequester.builder()
            .rsocketStrategies(builder -> builder.decoder(new Jackson2JsonDecoder()))
            .rsocketStrategies(builder -> builder.encoder(new Jackson2JsonEncoder()))
            .rsocketConnector(connector -> connector
                .payloadDecoder(PayloadDecoder.ZERO_COPY)
                .reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
            .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
            .tcp("localhost", 5200);
    }

    @AfterEach
    public void cleanup() {
        requester.dispose();
    }

    @Test
    public void testGetBook() {
        Book savedBook = bookRepository.save(createBook());
        Mono<Book> result = requester.route("getBook")
            .data(savedBook.getId())
            .retrieveMono(Book.class);
        assertNotNull(result.block());
    }

    @Test
    public void testAddBook() {
        Book book = createBook();
        Mono<Book> result = requester.route("addBook")
            .data(book)
            .retrieveMono(Book.class);
        Book savedBook = result.block();
        assertNotNull(savedBook);
        assertNotNull(savedBook.getId());
        assertTrue(savedBook.getId() > 0);
    }

    @Test
    public void testGetBooks() {
        Flux<Book> result = requester.route("getBooks")
            .retrieveFlux(Book.class);
        assertNotNull(result.blockFirst());
    }

    @Test
    public void testDeleteBook() {
        Book savedBook = bookRepository.save(createBook());
        Mono<Void> result = requester.route("deleteBook")
            .data(savedBook.getId())
            .send();
        result.block();
        Book deletedBook = bookRepository.findById(savedBook.getId()).get();
        assertNull(deletedBook);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private Book createBook() {
        Book book = new Book();
        book.setAuthor("Pushkin");
        book.setIsbn("SOME-ISBN");
        book.setYear(2024);
        book.setTitle("Stihi Pushkina Sobranie");
        book.setPublisher("Azbuka");
        return book;
    }

}
