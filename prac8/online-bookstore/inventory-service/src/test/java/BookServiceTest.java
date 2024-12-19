import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import inventoryservice.entity.Book;
import inventoryservice.repository.BookRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBookById_shouldReturnBook_whenBookExists() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test Title");
        book.setAuthor("Test Author");
        book.setStock(10);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(bookId);

        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertEquals("Test Title", result.getTitle());

        verify(bookRepository).findById(bookId);
    }

    @Test
    void getBookById_shouldReturnNull_whenBookDoesNotExist() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Book result = bookService.getBookById(bookId);

        assertNull(result);

        verify(bookRepository).findById(bookId);
    }

    @Test
    void saveBook_shouldSaveAndReturnBook() {
        Book book = new Book();
        book.setTitle("New Book");
        book.setAuthor("New Author");
        book.setStock(5);

        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.saveBook(book);

        assertNotNull(result);
        assertEquals("New Book", result.getTitle());
        assertEquals("New Author", result.getAuthor());
        assertEquals(5, result.getStock());

        verify(bookRepository).save(book);
    }
}
