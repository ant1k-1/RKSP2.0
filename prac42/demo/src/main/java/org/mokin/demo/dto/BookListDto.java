package org.mokin.demo.dto;

import org.mokin.demo.domain.Book;
import java.util.List;

public class BookListDto {
    private List<Book> books;
    public List<Book> getBooks() {
        return books;
    }
    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
