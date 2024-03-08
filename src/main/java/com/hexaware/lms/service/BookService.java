package com.hexaware.lms.service;

import com.hexaware.lms.dto.BookDto;
import com.hexaware.lms.entity.Book;
import com.hexaware.lms.exception.ResourceNotFoundException;


import java.util.List;
import java.util.Optional;

public interface BookService {
    //create / full update book
    BookDto save(BookDto bookDto);

    List<BookDto> findAll();

     BookDto findOne(Long id) throws ResourceNotFoundException;

    boolean isExists(Long id);

    BookDto partialUpdate(Long id, BookDto bookEntity) throws ResourceNotFoundException;

    void delete(Long id) throws ResourceNotFoundException;



    Optional<List<Book>> searchBarBook(String search) throws ResourceNotFoundException;

    Optional<List<Book>> searchByAuthor(String authorName) throws ResourceNotFoundException;

    Optional<List<Book>> searchByLanguage(String language) throws ResourceNotFoundException;

    Optional<List<Book>> findByCategory(String search) throws ResourceNotFoundException;

    BookDto fullUpdate(BookDto bookDto, Long id);

    Optional<List<String>> findCategory(Long id);

    Integer getLoanCount(Long id);

    Integer getReservationCount(Long userId);

    Integer findNoOfBooksLoanByEmail(String userEmail);
}
