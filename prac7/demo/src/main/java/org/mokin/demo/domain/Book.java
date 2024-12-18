package org.mokin.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("booksr")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    private Long id;
    @Column("title")
    private String title;
    @Column("author")
    private String author;
    @Column("isbn")
    private String isbn;
    @Column("publisher")
    private String publisher;
    @Column("year")
    private Integer year;
}
