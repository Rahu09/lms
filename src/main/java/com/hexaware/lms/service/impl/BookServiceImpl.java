package com.hexaware.lms.service.impl;


import com.hexaware.lms.Mapper.impl.BookMapper;
import com.hexaware.lms.dto.BookDto;
import com.hexaware.lms.entity.Book;
import com.hexaware.lms.entity.BookCategoryMapper;
import com.hexaware.lms.entity.Loan;
import com.hexaware.lms.entity.Reservation;
import com.hexaware.lms.exception.ResourceNotFoundException;
import com.hexaware.lms.repository.*;
import com.hexaware.lms.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {


    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final LoanRepository loanRepository;

    private final ReservationRepository reservationRepository;
    private final BookCategoryMapperRepository bookCategoryMapperRepository;


    //create/add book
    //full update book
    @Override
    public BookDto save(BookDto bookDto) {
        log.debug("Entered BokServiceImpl.save()  with arg: {} ", bookDto.toString());
        Book book = bookRepository.save(bookMapper.mapFrom(bookDto));
        BookDto bookDto1 = bookMapper.mapTo(book);
        log.debug("Exited BookServiceImpl.save()  with return data: {} ", bookDto1.toString());

        return bookDto1;

    }

    // read/get all books
    @Override
    public List<BookDto> findAll() {
        List<Book> book = bookRepository.findAll();
        //map to optional

        List<BookDto> bookDtos = book.stream()
                .map(bookMapper::mapTo)
                .collect(Collectors.toList());
        log.debug("Exited BookServiceImpl.findAll()  with return data: {} ", bookDtos.toString());
        return bookDtos;
    }

    // read/get by id
    @Override
    public BookDto findOne(Long id) throws ResourceNotFoundException {
        log.debug("Entered BokServiceImpl.findOne()  with arg: {} ", id);
        Optional<Book> optionalBook = bookRepository.findById(id);

        BookDto bookDto = null;
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            bookDto = bookMapper.mapTo(book);
        } else {
            throw new ResourceNotFoundException("book", "bookid", id);
        }
        log.debug("Exited BookServiceImpl.findAll()  with return data: {} ", bookDto.toString());
        return bookDto;
    }

    //check if book exists
    @Override
    public boolean isExists(Long id) {
        boolean exists = bookRepository.existsById(id);
        log.debug("Exited BookServiceImpl.isExists()  with return data: {} ", exists);
        return exists;
    }

    // partial book update
    @Override
    public BookDto partialUpdate(Long id, BookDto bookDto) throws ResourceNotFoundException {
        log.debug("Entered BokServiceImpl.partialUpdate()  with arg: {} and {} ", id, bookDto.toString());
        Book bookEntity = bookMapper.mapFrom(bookDto);


        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new ResourceNotFoundException("book", "bookid", id);
        }

        return book.map(existingBook -> {
            // Update existingBook with data from bookEntity
            Optional.ofNullable(bookEntity.getCost()).ifPresent(existingBook::setCost);
            Optional.ofNullable(bookEntity.getEdition()).ifPresent(existingBook::setEdition);
            Optional.ofNullable(bookEntity.getDescription()).ifPresent(existingBook::setDescription);
            Optional.ofNullable(bookEntity.getIsbn()).ifPresent(existingBook::setIsbn);
            Optional.ofNullable(bookEntity.getAuthorName()).ifPresent(existingBook::setAuthorName);
            Optional.ofNullable(bookEntity.getLanguage()).ifPresent(existingBook::setLanguage);
            Optional.ofNullable(bookEntity.getPublisherName()).ifPresent(existingBook::setPublisherName);
            Optional.ofNullable(bookEntity.getImageURL()).ifPresent(existingBook::setImageURL);
            Optional.ofNullable(bookEntity.getTitle()).ifPresent(existingBook::setTitle);
            Optional.ofNullable(bookEntity.getPages()).ifPresent(existingBook::setPages);
            Optional.ofNullable(bookEntity.getBookCount()).ifPresent(existingBook::setBookCount);

            // Save the updated bookEntity
            Book updatedBook = bookRepository.save(existingBook);
            BookDto bookDto1 = bookMapper.mapTo(updatedBook);

            // Convert the updated BookEntity back to a BookDto
            log.debug("Exited BookServiceImpl.partialUpdate()  with return data: {} ", bookDto1.toString());
            return bookDto1;
        }).orElse(null);

    }

    //delete by id
    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        log.debug("Entered BokServiceImpl.delete()  with arg: {}", id);
        if (!isExists(id)) {
            throw new ResourceNotFoundException("book", "bookid", id);
        }
        Reservation reservation = reservationRepository.findByBook(bookRepository.findById(id));
        Loan loan = loanRepository.findByBook(bookRepository.findById(id));
        BookCategoryMapper bookCategoryMapper = bookCategoryMapperRepository.findByBook(bookRepository.findById(id));

        if(reservation!=null)
        {
            reservation.setBook(null);
            reservationRepository.save(reservation);
        }
        if(loan!=null)
        {
            loan.setBook(null);
            loanRepository.save(loan);
        }

        if(bookCategoryMapper!=null)
        {
            bookCategoryMapper.setBook(null);
            bookCategoryMapperRepository.save(bookCategoryMapper);
        }

        log.debug("Exited BookServiceImpl.delete().");
        bookRepository.deleteById(id);
    }

    //search bar autosuggestions
    @Override
    public Optional<List<Book>> searchBarBook(String search) throws ResourceNotFoundException {
        log.debug("Entered BokServiceImpl.searchBarBook()  with arg: {}", search);
        Optional<List<Book>> optionalBookList = Optional.ofNullable(bookRepository.searchBook(search));

        if (optionalBookList.isEmpty()) {
            throw new ResourceNotFoundException("book", "bookid", 0L);
        }
        log.debug("Exited BookServiceImpl.searchBarBook() with return data: {} ", optionalBookList.toString());
        return optionalBookList;
    }

    @Override
    public Optional<List<Book>> searchByAuthor(String authorName) throws ResourceNotFoundException {
        log.debug("Entered BokServiceImpl.searchBarBook()  with arg: {}", authorName);

        Optional<List<Book>> optionalBookList = bookRepository.findByAuthorName(authorName);

        if (optionalBookList.isEmpty()) {
            throw new ResourceNotFoundException("book", "bookid", 0L);
        }
        log.debug("Exited BookServiceImpl.searchBarBook() with return data: {} ", optionalBookList.toString());
        return optionalBookList;
    }

    @Override
    public Optional<List<Book>> searchByLanguage(String language) throws ResourceNotFoundException {
        log.debug("Entered BokServiceImpl.searchBarBook()  with arg: {}", language);
        Optional<List<Book>> optionalBookList = bookRepository.findByLanguage(language);

        if (optionalBookList.isEmpty()) {
            throw new ResourceNotFoundException("book", "bookid", 0L);
        }
        log.debug("Exited BookServiceImpl.searchBarBook() with return data: {} ", optionalBookList.toString());
        return optionalBookList;
    }

    @Override
    public Optional<List<Book>> findByCategory(String category) throws ResourceNotFoundException {
        log.debug("Entered BokServiceImpl.searchBarBook()  with arg: {}", category);
        Optional<List<Book>> optionalBookList = bookRepository.findByCategory(category);

        if (optionalBookList.isEmpty()) {
            throw new ResourceNotFoundException("book", "bookid", 0L);
        }
        log.debug("Exited BookServiceImpl.searchBarBook() with return data: {} ", optionalBookList.toString());
        return optionalBookList;
    }

    @Override
    public BookDto fullUpdate(BookDto bookDto, Long id) {
        log.debug("Entered BokServiceImpl.searchBarBook()  with arg: {} and {}", id, bookDto.toString());
        bookDto.setId(id);
        Book book = bookRepository.save(bookMapper.mapFrom(bookDto));
        BookDto bookDto1 = bookMapper.mapTo(book);
        log.debug("Exited BookServiceImpl.searchBarBook() with return data: {} ", bookDto1.toString());
        return bookDto1;
    }


}
