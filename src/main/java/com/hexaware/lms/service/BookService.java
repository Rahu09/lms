package com.hexaware.lms.service;

import com.hexaware.lms.dto.BookDto;
import com.hexaware.lms.dto.BookFilterDto;
import com.hexaware.lms.dto.CategoryDTO;
import com.hexaware.lms.entity.Book;
import com.hexaware.lms.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BookService {
    //create / full update book
    BookDto save(BookDto bookDto) throws IOException;

    List<BookDto> findAll();

    Page<BookDto> findAll(Pageable pageable);

    BookDto findOne(Long id) throws ResourceNotFoundException;

//    boolean isExists(Long id);

//    BookDto partialUpdate(Long id, BookDto bookEntity) throws ResourceNotFoundException;

//    void delete(Long id) throws ResourceNotFoundException;

    List<BookDto> searchBarBook(String search) throws ResourceNotFoundException;

    Optional<List<Book>> searchByAuthor(String authorName) throws ResourceNotFoundException;

    Optional<List<Book>> searchByLanguage(String language) throws ResourceNotFoundException;

    Optional<List<Book>> findByCategory(String search) throws ResourceNotFoundException;

//    BookDto fullUpdate(BookDto bookDto, Long id);

    Optional<List<String>> findCategory(Long id);

    Integer getLoanCount(Long id);

    Integer getReservationCount(Long userId);

    Integer findNoOfBooksLoanByEmail(String userEmail);

    Page<BookDto> bookFilter(BookFilterDto bookFilterDto, Pageable pageable);

    void borrowBook(Long userId, Long bookId);

    void reserveBook(Long userId, Long bookId);

    List<String> getAllUniqueAuthorNames();

    List<String> getAllUniqueLanguages();

    List<String> getAllBooksName();

    List<String> findAllCategory();
}