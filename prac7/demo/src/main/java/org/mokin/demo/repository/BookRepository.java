package org.mokin.demo.repository;

import org.mokin.demo.domain.Book;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends R2dbcRepository<Book, Long> {
}
