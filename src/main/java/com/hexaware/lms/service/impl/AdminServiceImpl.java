package com.hexaware.lms.service.impl;

import com.hexaware.lms.Mapper.impl.CategoryMapper;
import com.hexaware.lms.Mapper.impl.NotificationMapper;
import com.hexaware.lms.dto.*;
import com.hexaware.lms.entity.*;
import com.hexaware.lms.exception.ResourceNotFoundException;
import com.hexaware.lms.repository.*;
import com.hexaware.lms.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
}