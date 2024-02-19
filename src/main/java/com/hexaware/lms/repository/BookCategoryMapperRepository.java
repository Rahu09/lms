package com.hexaware.lms.repository;

import com.hexaware.lms.entity.Book;
import com.hexaware.lms.entity.BookCategoryMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookCategoryMapperRepository extends JpaRepository<BookCategoryMapper,Long> {
    BookCategoryMapper findByBook(Optional<Book> byId);
}
