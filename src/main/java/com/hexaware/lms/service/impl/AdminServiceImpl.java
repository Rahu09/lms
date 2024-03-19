package com.hexaware.lms.service.impl;

import com.hexaware.lms.Mapper.impl.BookMapper;
import com.hexaware.lms.Mapper.impl.CategoryMapper;
import com.hexaware.lms.Mapper.impl.NotificationMapper;
import com.hexaware.lms.dto.*;
import com.hexaware.lms.entity.*;
import com.hexaware.lms.exception.ResourceNotFoundException;
import com.hexaware.lms.repository.*;
import com.hexaware.lms.service.AdminService;
import com.hexaware.lms.utils.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationRepository notificationRepository;
    private final CategoryMapper categoryMapper;
    private final NotificationMapper notificationMapper;
    private final BookMapper bookMapper;
    private final BookCategoryMapperRepository bookCategoryMapperRepository;
    @Override
    public List<CategoryDTO> findAll() {
        log.debug("entered AdminServiceImpl.findAll() service");
        List<Category> resultEntity = categoryRepository.findAll();
        List<CategoryDTO> resultDto = resultEntity.stream().map(it-> categoryMapper.mapTo(it)).collect(Collectors.toList());

        log.debug("exiting AdminServiceImpl.findAll() service with return data: {}", resultDto.toString());
        return resultDto;
    }

    @Override
    public CategoryDTO addCategory(AddCategoryRequestBody request) {
        log.debug("entered AdminServiceImpl.addCategory() service with args: {}",request.toString());

        Category newCategory = new Category(request.getCategory());
        Category savedCategory = categoryRepository.save(newCategory);
        CategoryDTO savedCategoryDTO = categoryMapper.mapTo(savedCategory);

        log.debug("exiting AdminServiceImpl.addCategory() service with return data: {}", savedCategoryDTO.toString());
        return savedCategoryDTO;
    }

    @Override
    public void deleteCategory(long id) throws ResourceNotFoundException {
        log.debug("entered AdminServiceImpl.deleteCategory() service with args: {}",id);

        if(!categoryRepository.existsById(id)) throw new ResourceNotFoundException("bookId","id",id);
        categoryRepository.deleteById(id);

        log.debug("exiting AdminServiceImpl.deleteCategory() service ");
    }

    @Override
    public NotificationDTO sendReturnRequest(NotificationDTO notificationDTO, long id) throws ResourceNotFoundException {
        log.debug("entered AdminServiceImpl.sendReturnRequest() service with args: {} and {}",notificationDTO.toString(),id);

        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) throw new ResourceNotFoundException("userId","id",notificationDTO.getId());
        notificationDTO.setUser(user.get());

        Notification notificationEntity = notificationMapper.mapFrom(notificationDTO);

        Notification savedEntity = notificationRepository.save(notificationEntity);
        NotificationDTO savedNotificationDTO = notificationMapper.mapTo(savedEntity);
        log.debug("exiting AdminServiceImpl.sendReturnRequest() service with return data: {}", savedNotificationDTO.toString());
        return savedNotificationDTO;
    }

    @Override
    public List<BookLoanHistoryDTO> getBookLoanHistory(long bookId) throws ResourceNotFoundException {
        log.debug("entered AdminServiceImpl.getBookLoanHistory() service with args: {}",bookId);

        Optional<Book> book = bookRepository.findById(bookId);
        if(book.isEmpty()) throw new ResourceNotFoundException("book","id",bookId);


        List<Loan> loanList = loanRepository.findAllByBook(book.get());
        log.info("loan list found");

        List<BookLoanHistoryDTO> BookLoanHistoryList = loanList.stream().map(it-> {
            User user = it.getUser();
            String userName=user.getFirstName()+user.getLastName();

            return BookLoanHistoryDTO.builder()
                    .user(userName)
                    .id(it.getId())
                    .issueDate(it.getIssueDate())
                    .returnDate(it.getReturnDate())
                    .status(it.getStatus())
            .build();
        }).collect(Collectors.toList());
        log.debug("exiting AdminServiceImpl.getBookLoanHistory() service with return data: {}", BookLoanHistoryList.toString());
        return BookLoanHistoryList;
    }

    @Override
    public List<BookReservationHistoryDTO> getBookReservationHistory(long bookId) throws ResourceNotFoundException {
        log.debug("entered AdminServiceImpl.getBookReservationHistory() service with args: {}",bookId);
        Optional<Book> book = bookRepository.findById(bookId);
        if(book.isEmpty()) throw new ResourceNotFoundException("book","id",bookId);


        List<Reservation> reserveList = reservationRepository.findAllByBook(book.get());
        log.info("loan list found");

        List<BookReservationHistoryDTO> BookReservationHistoryList = reserveList.stream().map(it-> {
            User user = it.getUser();
            String userName=user.getFirstName()+user.getLastName();

            return BookReservationHistoryDTO.builder()
                    .user(userName)
                    .id(it.getId())
                    .issueTimestamp(it.getIssueTimestamp())
                    .build();
        }).collect(Collectors.toList());

        log.debug("exiting AdminServiceImpl.getBookReservationHistory() service with return data: {}", BookReservationHistoryList.toString());
        return BookReservationHistoryList;
    }

    @Override
    public List<UserLoanHistoryDTO> getUserLoanHistory(long userId) throws ResourceNotFoundException {
        log.debug("entered AdminServiceImpl.getUserLoanHistory() service with args: {}",userId);
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new ResourceNotFoundException("user","id",userId);


        List<Loan> loanList = loanRepository.findAllByUser(user.get());

        List<UserLoanHistoryDTO> UserLoanHistoryList = loanList.stream().map(it-> {

            Book book = it.getBook();
            String bookName=book.getTitle();

            return UserLoanHistoryDTO.builder()
                    .book(bookName)
                    .id(it.getId())
                    .issueDate(it.getIssueDate())
                    .returnDate(it.getReturnDate())
                    .status(it.getStatus())
                    .build();
        }).collect(Collectors.toList());

        log.debug("exiting AdminServiceImpl.getUserLoanHistory() service with return data: {}", UserLoanHistoryList.toString());
        return UserLoanHistoryList;
    }

    @Override
    public List<fineDTO> getUserFine(long userId) throws ResourceNotFoundException {
        log.debug("entered AdminServiceImpl.getUserFine() service with args: {}",userId);
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new ResourceNotFoundException("user","id",userId);

        List<Loan> loanList = loanRepository.findAllByUser(user.get());
        log.info("loan list found");

        List<fineDTO> userFineList = loanList.stream().map(it->{

            Book book = it.getBook();
            String bookName = book.getTitle();

            User finedUser = it.getUser();
            String userName = finedUser.getFirstName()+finedUser.getLastName();

            Duration duration = Duration.between(it.getIssueDate(),it.getReturnDate());
            Long fine = (duration.toDays() <=7) ? 0 : (duration.toDays()-7) * 5;

            return fineDTO.builder()
                    .book(bookName)
                    .user(userName)
                    .id(it.getId())
                    .issueDate(it.getIssueDate())
                    .returnDate(it.getReturnDate())
                    .status(it.getStatus())
                    .fineAmount(fine)
                    .build();
        }).collect(Collectors.toList());

        log.debug("exiting AdminServiceImpl.getUserFine() service with return data: {}", userFineList.toString());
        return userFineList;
    }

    @Override
    public List<fineDTO> getTotalFine() throws ResourceNotFoundException {
        log.debug("entered AdminServiceImpl.getTotalFine() service ");
        List<Loan> loanList = loanRepository.overdueLoans();

        List<fineDTO> totalFineList = loanList.stream().map(it->{

            Book book = it.getBook();
            String bookName = book.getTitle();
            User finedUser = it.getUser();
            String userName = finedUser.getFirstName()+finedUser.getLastName();

            Duration duration = Duration.between(it.getIssueDate(),it.getReturnDate());
            Long fine = (duration.toDays() <=7) ? 0 : (duration.toDays()-7) * 5;

            return fineDTO.builder()
                    .book(bookName)
                    .user(userName)
                    .id(it.getId())
                    .issueDate(it.getIssueDate())
                    .returnDate(it.getReturnDate())
                    .status(it.getStatus())
                    .fineAmount(fine)
                    .build();
        }).collect(Collectors.toList());

        log.debug("exiting AdminServiceImpl.getTotalFine() service with return data: {}", totalFineList.toString());
        return totalFineList;
    }

    @Override
    public Integer getLoanWarningCount() {
        OffsetDateTime today = OffsetDateTime.now().truncatedTo(ChronoUnit.DAYS); // Today
        OffsetDateTime issueDate = today.minusDays(6); // 6 days ago
        return loanRepository.getLoanCountForDate(issueDate);
    }

    @Override
    public List<LoanDto> getLateLoan() {
        List<Loan> lateLoanList = loanRepository.findAllLateLoans();

        //convert to dto
        List<LoanDto> lateLoanDtoList =lateLoanList.stream().map(it->{

            return LoanDto.builder()
                    .status(it.getStatus())
                    .book(it.getBook())
                    .issueDate(it.getIssueDate())
                    .returnDate(it.getReturnDate())
                    .user(it.getUser())
                    .build();
        }).collect(Collectors.toList());
        return lateLoanDtoList;
    }

    @Override
    public void createAlertRequest() {

        //find all loans with date 7 days from today
        OffsetDateTime today = OffsetDateTime.now().truncatedTo(ChronoUnit.DAYS); // Today
        OffsetDateTime issueDate = today.minusDays(7); // 7 days ago


        //get all exact 7 days old loans
        List<Loan> loanList = loanRepository.getAllLoansIssueDate(issueDate);

        // create alert notification for all
        for (Loan loan : loanList) {
            Long userId = loan.getUser().getId();
            Long bookId = loan.getBook().getId();

            Optional<Book> bookOptional = bookRepository.findById(bookId);
            Optional<User> userOptional = userRepository.findById(userId);

            if (bookOptional.isPresent() && userOptional.isPresent()) {
                String bookName = bookOptional.get().getTitle();
                User user = userOptional.get();

                Notification notification = Notification.builder()
                        .message("Please return " + bookName+".")
                        .type(NotificationType.ALERT)
                        .seen(false)
                        .user(user)
                        .build();

                notificationRepository.save(notification);
            }
        }
        //set status as late
        loanRepository.updateStatusToLateByIssueDate(issueDate);
    }

    @Override
    public void createReminderRequest() {

        //find all loans with date 6 days from today
        OffsetDateTime today = OffsetDateTime.now().truncatedTo(ChronoUnit.DAYS); // Today
        OffsetDateTime issueDate = today.minusDays(6); // 6 days ago


        //get all loans with status = LOAN , which have issue date as given issue date
        List<Loan> loanList = loanRepository.getAllLoansIssueDate(issueDate);

        //now create reminder notification for all
        for (Loan loan : loanList) {
            Long userId = loan.getUser().getId();
            Long bookId = loan.getBook().getId();

            Optional<Book> bookOptional = bookRepository.findById(bookId);
            Optional<User> userOptional = userRepository.findById(userId);

            if (bookOptional.isPresent() && userOptional.isPresent()) {
                String bookName = bookOptional.get().getTitle();
                User user = userOptional.get();

                Notification notification = Notification.builder()
                        .message("Please return " + bookName + " by tomorrow.")
                        .type(NotificationType.REMINDER)
                        .seen(false)
                        .user(user)
                        .build();

                notificationRepository.save(notification);
            }
        }
    }

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
    public boolean isExists(Long id) {
        boolean exists = bookRepository.existsById(id);
        log.debug("Exited BookServiceImpl.isExists()  with return data: {} ", exists);
        return exists;
    }
}