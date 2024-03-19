package com.hexaware.lms.service.impl;


import com.hexaware.lms.Mapper.impl.BookMapper;
import com.hexaware.lms.Mapper.impl.CategoryMapper;
import com.hexaware.lms.dto.BookDto;
import com.hexaware.lms.dto.BookFilterDto;
import com.hexaware.lms.dto.CategoryDTO;
import com.hexaware.lms.entity.*;
import com.hexaware.lms.exception.ResourceNotFoundException;
import com.hexaware.lms.repository.*;
import com.hexaware.lms.service.BookService;
import com.hexaware.lms.utils.LoanStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Base64;
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
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    //create/add book
    //full update book
    @Override
    public BookDto save(BookDto bookDto) throws IOException {
        log.debug("Entered BokServiceImpl.save()  with arg: {} ", bookDto.toString());
        Book book = bookRepository.save(bookMapper.mapFrom(bookDto));
        BookDto savedBookDto = bookMapper.mapTo(book);
        log.debug("Exited BookServiceImpl.save()  with return data: {} ", savedBookDto.toString());

        return savedBookDto;
    }

    // read/get all books
    @Override
    public List<BookDto> findAll() {
        List<Book> book = bookRepository.findAll();
        //map to optional

        List<BookDto> bookDtos = book.stream()
                .map(it->{
                    String base64Image = "";
                    try{
                        File imageFile = new File("E:/production/hexa/lms/lms/src/main/resources/static/"+it.getImageURL());

                        FileInputStream fileInputStreamReader = new FileInputStream(imageFile);

                        byte[] imageData = new byte[(int) imageFile.length()];

                        fileInputStreamReader.read(imageData);

                        fileInputStreamReader.close();

                        base64Image = Base64.getEncoder().encodeToString(imageData);
                    } catch (Exception e){
                        log.info("error in finding image");
                    }

                    return BookDto.builder()
                            .imageURL(base64Image)
                            .title(it.getTitle())
                            .publisherName(it.getPublisherName())
                            .pages(it.getPages())
                            .link(it.getLink())
                            .language(it.getLanguage())
                            .isbn(it.getIsbn())
                            .edition(it.getEdition())
                            .id(it.getId())
                            .cost(it.getCost())
                            .bookCount(it.getBookCount())
                            .authorName(it.getAuthorName())
                            .description(it.getDescription())
                            .build();
                })
                .collect(Collectors.toList());
        log.debug("Exited BookServiceImpl.findAll()  with return data: {} ", bookDtos.toString());
        return bookDtos;
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        Page<Book> book = bookRepository.findAll(pageable);

        Page<BookDto> bookDtos = book
                .map(it->{
                    String base64Image = "";
                    try{
                        File imageFile = new File("E:/production/hexa/lms/lms/src/main/resources/static/"+it.getImageURL());
                        FileInputStream fileInputStreamReader = new FileInputStream(imageFile);
                        byte[] imageData = new byte[(int) imageFile.length()];
                        fileInputStreamReader.read(imageData);
                        fileInputStreamReader.close();
                        base64Image = Base64.getEncoder().encodeToString(imageData);
                    } catch (Exception e){
                        log.info("error in finding image");
                    }

                    return BookDto.builder()
                            .imageURL(base64Image)
                            .title(it.getTitle())
                            .publisherName(it.getPublisherName())
                            .pages(it.getPages())
                            .link(it.getLink())
                            .language(it.getLanguage())
                            .isbn(it.getIsbn())
                            .edition(it.getEdition())
                            .id(it.getId())
                            .cost(it.getCost())
                            .bookCount(it.getBookCount())
                            .authorName(it.getAuthorName())
                            .description(it.getDescription())
                            .build();
                });
        log.debug("Exited BookServiceImpl.findAll()  with return data: {} ", bookDtos.toString());
        return bookDtos;
    }

    @Override
    public Page<BookDto> bookFilter(BookFilterDto bookFilterDto, Pageable pageable) {
        log.info(bookFilterDto.getLanguageList().toString());
        Page<Book> bookList = bookRepository.findByFilters(bookFilterDto.getAuthorList(),bookFilterDto.getLanguageList(),bookFilterDto.getCategoryList(),pageable);

        Page<BookDto> bookDtos = bookList
                .map(it->{
                    String base64Image = "";
                    try{
                        File imageFile = new File("E:/production/hexa/lms/lms/src/main/resources/static/"+it.getImageURL());
                        FileInputStream fileInputStreamReader = new FileInputStream(imageFile);
                        byte[] imageData = new byte[(int) imageFile.length()];
                        fileInputStreamReader.read(imageData);
                        fileInputStreamReader.close();
                        base64Image = Base64.getEncoder().encodeToString(imageData);
                    } catch (Exception e){
                        log.info("error in finding image");
                    }

                    return BookDto.builder()
                            .imageURL(base64Image)
                            .title(it.getTitle())
                            .publisherName(it.getPublisherName())
                            .pages(it.getPages())
                            .link(it.getLink())
                            .language(it.getLanguage())
                            .isbn(it.getIsbn())
                            .edition(it.getEdition())
                            .id(it.getId())
                            .cost(it.getCost())
                            .bookCount(it.getBookCount())
                            .authorName(it.getAuthorName())
                            .description(it.getDescription())
                            .build();
                });
        log.debug("Exited BookServiceImpl.bookFilter()  with return data: {} ", bookDtos.toString());
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
            try{
                File imageFile = new File("E:/production/hexa/lms/lms/src/main/resources/static/"+bookDto.getImageURL());
                FileInputStream fileInputStreamReader = new FileInputStream(imageFile);
                byte[] imageData = new byte[(int) imageFile.length()];
                fileInputStreamReader.read(imageData);
                fileInputStreamReader.close();
                String base64Image = Base64.getEncoder().encodeToString(imageData);
                bookDto.setImageURL(base64Image);
            } catch (Exception e){
                log.info("error in finding image");
            }
        } else {
            throw new ResourceNotFoundException("book", "bookid", id);
        }
        log.debug("Exited BookServiceImpl.findAll()  with return data: {} ", bookDto.toString());
        return bookDto;
    }

    //check if book exists
//    @Override
//    public boolean isExists(Long id) {
//        boolean exists = bookRepository.existsById(id);
//        log.debug("Exited BookServiceImpl.isExists()  with return data: {} ", exists);
//        return exists;
//    }

    // partial book update
//    @Override
//    public BookDto partialUpdate(Long id, BookDto bookDto) throws ResourceNotFoundException {
//        log.debug("Entered BokServiceImpl.partialUpdate()  with arg: {} and {} ", id, bookDto.toString());
//        Book bookEntity = bookMapper.mapFrom(bookDto);
//
//
//        Optional<Book> book = bookRepository.findById(id);
//        if (book.isEmpty()) {
//            throw new ResourceNotFoundException("book", "bookid", id);
//        }
//
//        return book.map(existingBook -> {
//            // Update existingBook with data from bookEntity
//            Optional.ofNullable(bookEntity.getCost()).ifPresent(existingBook::setCost);
//            Optional.ofNullable(bookEntity.getEdition()).ifPresent(existingBook::setEdition);
//            Optional.ofNullable(bookEntity.getDescription()).ifPresent(existingBook::setDescription);
//            Optional.ofNullable(bookEntity.getIsbn()).ifPresent(existingBook::setIsbn);
//            Optional.ofNullable(bookEntity.getAuthorName()).ifPresent(existingBook::setAuthorName);
//            Optional.ofNullable(bookEntity.getLanguage()).ifPresent(existingBook::setLanguage);
//            Optional.ofNullable(bookEntity.getPublisherName()).ifPresent(existingBook::setPublisherName);
//            Optional.ofNullable(bookEntity.getImageURL()).ifPresent(existingBook::setImageURL);
//            Optional.ofNullable(bookEntity.getTitle()).ifPresent(existingBook::setTitle);
//            Optional.ofNullable(bookEntity.getPages()).ifPresent(existingBook::setPages);
//            Optional.ofNullable(bookEntity.getBookCount()).ifPresent(existingBook::setBookCount);
//
//            // Save the updated bookEntity
//            Book updatedBook = bookRepository.save(existingBook);
//            BookDto bookDto1 = bookMapper.mapTo(updatedBook);
//
//            // Convert the updated BookEntity back to a BookDto
//            log.debug("Exited BookServiceImpl.partialUpdate()  with return data: {} ", bookDto1.toString());
//            return bookDto1;
//        }).orElse(null);
//
//    }

    //delete by id
//    @Override
//    public void delete(Long id) throws ResourceNotFoundException {
//        log.debug("Entered BokServiceImpl.delete()  with arg: {}", id);
//        if (!isExists(id)) {
//            throw new ResourceNotFoundException("book", "bookid", id);
//        }
//        Reservation reservation = reservationRepository.findByBook(bookRepository.findById(id));
//        Loan loan = loanRepository.findByBook(bookRepository.findById(id));
//        BookCategoryMapper bookCategoryMapper = bookCategoryMapperRepository.findByBook(bookRepository.findById(id));
//
//        if(reservation!=null)
//        {
//            reservation.setBook(null);
//            reservationRepository.save(reservation);
//        }
//        if(loan!=null)
//        {
//            loan.setBook(null);
//            loanRepository.save(loan);
//        }
//
//        if(bookCategoryMapper!=null)
//        {
//            bookCategoryMapper.setBook(null);
//            bookCategoryMapperRepository.save(bookCategoryMapper);
//        }
//
//        log.debug("Exited BookServiceImpl.delete().");
//        bookRepository.deleteById(id);
//    }

    //search bar autosuggestions
    @Override
    public List<BookDto> searchBarBook(String search) throws ResourceNotFoundException {
        log.debug("Entered BokServiceImpl.searchBarBook()  with arg: {}", search);
        Optional<List<Book>> optionalBookList = Optional.ofNullable(bookRepository.searchBook(search));

        if (optionalBookList.isEmpty()) {
            throw new ResourceNotFoundException("book", "bookid", 0L);
        }
        List<BookDto> bookDtoList = optionalBookList.get().stream().map(it->{

            String base64Image = "";
            try{
                File imageFile = new File("E:/production/hexa/lms/lms/src/main/resources/static/"+it.getImageURL());

                FileInputStream fileInputStreamReader = new FileInputStream(imageFile);

                byte[] imageData = new byte[(int) imageFile.length()];

                fileInputStreamReader.read(imageData);

                fileInputStreamReader.close();

                base64Image = Base64.getEncoder().encodeToString(imageData);
            } catch (Exception e){
                log.info("error in finding image");
            }

            return BookDto.builder()
                    .bookCount(it.getBookCount())
                    .description(it.getDescription())
                    .authorName(it.getAuthorName())
                    .cost(it.getCost())
                    .id(it.getId())
                    .edition(it.getEdition())
                    .isbn(it.getIsbn())
                    .language(it.getLanguage())
                    .link(it.getLink())
                    .pages(it.getPages())
                    .publisherName(it.getPublisherName())
                    .title(it.getTitle())
                    .imageURL(base64Image)
                    .build();
        }).collect(Collectors.toList());
        log.debug("Exited BookServiceImpl.searchBarBook() with return data: {} ", optionalBookList.toString());
        return bookDtoList;
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

//    @Override
//    public BookDto fullUpdate(BookDto bookDto, Long id) {
//        log.debug("Entered BokServiceImpl.searchBarBook()  with arg: {} and {}", id, bookDto.toString());
//        bookDto.setId(id);
//        Book book = bookRepository.save(bookMapper.mapFrom(bookDto));
//        BookDto bookDto1 = bookMapper.mapTo(book);
//        log.debug("Exited BookServiceImpl.searchBarBook() with return data: {} ", bookDto1.toString());
//        return bookDto1;
//    }

    @Override
    public Optional<List<String>> findCategory(Long id){
        log.debug("Entered BokServiceImpl.findCategory()  with arg: {}", id);
        Optional<List<String>> category = bookRepository.findCategoryByBookId(id);
        log.debug("Exited BookServiceImpl.findCategory() with return data: {} ", category);
        return category;
    }

    @Override
    public Integer getLoanCount(Long id) {
        log.debug("Entered BokServiceImpl.getLoanCount()  with arg: {}", id);
        Integer loanCount = loanRepository.getLoanCount(id);
        log.debug("Exited BookServiceImpl.getLoanCount() with return data: {} ", loanCount);
        return loanCount;
    }

    @Override
    public Integer getReservationCount(Long id) {
        log.debug("Entered BokServiceImpl.getReservationCount()  with arg: {}", id);
        Integer reservationCount = reservationRepository.getReservationCount(id);
        log.debug("Exited BookServiceImpl.getReservationCount() with return data: {} ", reservationCount);
        return reservationCount;
    }

    @Override
    public Integer findNoOfBooksLoanByEmail(String userEmail) {
        log.debug("Entered BokServiceImpl.getReservationCount()  with arg: {}", userEmail);
        Integer noOfBooksLoan = userRepository.findNoOfBooksLoanByEmail(userEmail);
        log.debug("Exited BookServiceImpl.getReservationCount() with return data: {} ", noOfBooksLoan);
        return noOfBooksLoan;
    }

    @Override
    public void borrowBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + bookId + " not found"));

        Loan loan = Loan.builder()
                .user(user)
                .book(book)
                .issueDate(OffsetDateTime.now())
                .returnDate(null)
                .status(LoanStatus.LOAN)
                .build();
        //reduce the book count in book table by 1
        book.setBookCount(book.getBookCount() - 1);
        user.setNoOfBooksLoan(user.getNoOfBooksLoan()+1);
        bookRepository.save(book);
        userRepository.save(user);
        loanRepository.save(loan);
    }

    public void reserveBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + bookId + " not found"));

        Reservation reservation = Reservation.builder()
                .user(user)
                .book(book)
                .issueTimestamp(OffsetDateTime.now())
                .build();

        reservationRepository.save(reservation);
    }

    @Override
    public List<String> getAllUniqueAuthorNames() {
        return bookRepository.findDistinctAuthorNames();
    }

    @Override
    public List<String> getAllUniqueLanguages() {
        return bookRepository.findDistinctLanguages();
    }

    @Override
    public List<String> getAllBooksName() {
        return bookRepository.findDistinctBookNames();
    }

    @Override
    public List<String> findAllCategory() {
        log.debug("entered AdminServiceImpl.findAll() service");
        List<Category> resultEntity = categoryRepository.findAll();
        List<String> resultDto = resultEntity.stream().map(it-> it.getCategory()).collect(Collectors.toList());

        log.debug("exiting AdminServiceImpl.findAll() service with return data: {}", resultDto.toString());
        return resultDto;
    }

}
