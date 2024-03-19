package com.hexaware.lms.service.impl;

import com.hexaware.lms.Mapper.impl.NotificationMapper;
import com.hexaware.lms.Mapper.impl.UserMapper;
import com.hexaware.lms.dto.*;
import com.hexaware.lms.entity.*;
import com.hexaware.lms.exception.LoanLimitReachedException;
import com.hexaware.lms.exception.ResourceNotFoundException;
import com.hexaware.lms.repository.*;
import com.hexaware.lms.service.UserService;
import com.hexaware.lms.exception.AmountInsufficientException;
import com.hexaware.lms.utils.LoanStatus;
import com.hexaware.lms.utils.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Optional;

import static com.hexaware.lms.utils.LoanStatus.LOAN;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserMapper userMapper;
    private final NotificationRepository notificationRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public Reservation createReservation(Long userid, Long bookid) throws ResourceNotFoundException {

        log.debug("Entered UserServiceImpl.createReservation()  with arg: {} and {} ",userid,bookid);
        Optional<User> user =  userRepository.findById(userid);
        Optional<Book> book = bookRepository.findById(bookid);

        //check if user and book id is present, isPresent()
        if(user.isEmpty())
        {
            throw new ResourceNotFoundException("user","userid",userid);
        }
        if(book.isEmpty())
        {
            throw new ResourceNotFoundException("book","bookid",bookid);
        }


        Reservation createdReservation = Reservation.builder()
                .user(user.get())
                .book(book.get())
                .build();
        log.debug("Exited UserServiceImpl.createReservation()  with return data: {} ",createdReservation.toString());
        return createdReservation;
    }

    @Override
    public Loan createLoan(Long userid, Long bookid) throws ResourceNotFoundException, LoanLimitReachedException {
        log.debug("Entered UserServiceImpl.createLoan()  with arg: {} and {} ",userid,bookid);
        Optional<User> user =  userRepository.findById(userid);
        Optional<Book> book = bookRepository.findById(bookid);

        //check if user and book id is present, isPresent()
        if(user.isEmpty())
        {
            throw new ResourceNotFoundException("user","userid",userid);
        }
        if(book.isEmpty())
        {
            throw new ResourceNotFoundException("book","bookid",bookid);
        }

        //check if 5 books have been loaned already
        int loanedbooks = userRepository.findById(userid).get().getNoOfBooksLoan();
        Loan createdLoan = Loan.builder()
                .user(user.get())
                .book(book.get())
                .returnDate(null)
                .status(LOAN)
                .build();
        if(loanedbooks==5)
        {
            throw new LoanLimitReachedException("Cannot Loan more than 5 books at a time.");
        }
        log.debug("Exited UserServiceImpl.Loan()  with return data: {} ",createdLoan.toString());
        return createdLoan;
    }

    @Override
    public List<UserLoanHistoryDTO> getUserLoanHistory(long userId) throws ResourceNotFoundException {
        log.debug("entered UserServiceImpl.getUserLoanHistory() service with args: {}",userId);
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new ResourceNotFoundException("user","id",userId);


        List<Loan> loanList = loanRepository.findAllByUser(user.get());
        log.info("loan list found");

        List<UserLoanHistoryDTO> UserLoanHistoryList = loanList.stream().map(it-> {

            Book book = it.getBook();
            String bookName="";
            if(book ==null) {
                log.info("user not found");
            }else {
                bookName = book.getTitle();
            }

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
        log.debug("entered UserServiceImpl.getUserFine() service with args: {}",userId);
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new ResourceNotFoundException("user","id",userId);

        List<Loan> loanList = loanRepository.findAllByUser(user.get());
//        log.info("loan list found");
//        log.info("Loan list size: {}", loanList.size());
//        for (Loan loan : loanList) {
//            log.info("Loan details: {}", loan.toString());
//        }

        List<fineDTO> userFineList = loanList.stream().map(it->{

            Book book = it.getBook();
            String bookName = book.getTitle();
            User finedUser = it.getUser();
            String userName = finedUser.getFirstName()+finedUser.getLastName();

            String base64Image = "";
            try{
                File imageFile = new File("E:/production/hexa/lms/lms/src/main/resources/static/"+book.getImageURL());

                FileInputStream fileInputStreamReader = new FileInputStream(imageFile);

                byte[] imageData = new byte[(int) imageFile.length()];

                fileInputStreamReader.read(imageData);

                fileInputStreamReader.close();

                base64Image = Base64.getEncoder().encodeToString(imageData);
            } catch (Exception e){
                log.info("error in finding image");
            }

            Duration duration;
            if (it.getReturnDate() != null) {
                duration = Duration.between(it.getIssueDate(), it.getReturnDate());
            } else {
                // Assuming current time as return date when it is null
                duration = Duration.between(it.getIssueDate(), Instant.now().atOffset(ZoneOffset.UTC));
            }
            Long fine = (Math.abs(duration.toDays()) <=7) ? 0 : (Math.abs(duration.toDays())-7) * 5;
            log.info(Math.abs(duration.toDays())+"");
            log.info(fine.toString());

            return fineDTO.builder()
                    .book(bookName)
                    .user(userName)
                    .id(it.getId())
                    .imageUrl(base64Image)
                    .bookId(book.getId())
                    .issueDate(it.getIssueDate())
                    .returnDate(it.getReturnDate())
                    .status(it.getStatus())
                    .fineAmount(fine)
                    .build();
        }).collect(Collectors.toList());


        log.debug("exiting UserServiceImpl.getUserFine() service with return data: {}", userFineList.toString());
        return userFineList;
    }


    @Override
    public void setStatusLost(long loanId) throws ResourceNotFoundException,IllegalArgumentException {
        log.debug("entered UserServiceImpl.setStatusLost() service with args: {}",loanId);
        Optional<Loan> loan = loanRepository.findById(loanId);
        if(loan.isEmpty()) throw new ResourceNotFoundException("loan","loanId",loanId);
        if(loan.get().getStatus() != LoanStatus.LOAN) throw new IllegalArgumentException(String.format("loanStatus is: %s required LOST",loan.get().getStatus()));

        loan.get().setStatus(LoanStatus.LOST);
        loanRepository.save(loan.get());

        log.debug("exiting AdminServiceImpl.setStatusLost() service ");
    }

    @Override
    public SubmitBookDTO submitBook(long loanId, long amountSubmitted) throws ResourceNotFoundException, AmountInsufficientException {
        log.debug("entered UserServiceImpl.submitBook() service with args: {} and {}",loanId,amountSubmitted);
        Optional<Loan> loan = loanRepository.findById(loanId);
        if(loan.isEmpty()) throw new ResourceNotFoundException("loan","loanId",loanId);
        if(loan.get().getStatus() != LoanStatus.LOAN) throw new IllegalArgumentException(String.format("loanStatus is: %s required LOST",loan.get().getStatus()));

        Duration duration = Duration.between(loan.get().getIssueDate(), OffsetDateTime.now());
        Long fine = (duration.toDays() <=7) ? 0 : (duration.toDays()-7) * 5;

        if(amountSubmitted<fine) throw new AmountInsufficientException("Amount Submitted","amountSubmitted",amountSubmitted);
        loan.get().setStatus(LoanStatus.RETURNED);
        loan.get().setReturnDate(OffsetDateTime.now());
        loanRepository.save(loan.get());

        SubmitBookDTO submitBookDTO = SubmitBookDTO.builder()
                .bookId(loan.get().getBook().getId())
                .returnAmount(amountSubmitted-fine)
                .id(loanId)
                .submittedAmount(amountSubmitted)
                .issueDate(loan.get().getIssueDate())
                .returnDate(loan.get().getReturnDate())
                .status(loan.get().getStatus())
                .userId(loan.get().getUser().getId())
                .build();

        Long bookId = loan.get().getBook().getId();
        Book book = loan.get().getBook();
        User user  = loan.get().getUser();

        // as soon as the user submits the book the bookcount in book entity should be increased by one
        book.setBookCount(book.getBookCount()+1);
        bookRepository.save(book);

        //userbookcount -1
        user.setNoOfBooksLoan(user.getNoOfBooksLoan()-1);
        userRepository.save(user);

        //after that the reservation table for that book id should be checked and get the oldest one
        Optional<Reservation> optionalReservation = reservationRepository.findOldestReservationByBookId(bookId);
        if(optionalReservation.isPresent()) //if a reservation exists then do the below steps otherwise skip
        {
            Reservation reservation = optionalReservation.get();
            Book reservationBook  = reservation.getBook();
            User reservationUser = reservation.getUser();

            //after that create a loan from that reservation details
            Loan loan1 = Loan.builder()
                    .issueDate(OffsetDateTime.now())
                    .returnDate(null)
                    .status(LOAN)
                    .book(reservation.getBook())
                    .user(reservation.getUser())
                    .build();

            //decrease the bookcount by one after reserving
            book.setBookCount(book.getBookCount()-1);
            bookRepository.save(book);

            //userbookcount -1
            user.setNoOfBooksLoan(user.getNoOfBooksLoan()-1);
            userRepository.save(user);

            //then delete the reservation from the table
            reservationRepository.deleteReservationById(reservation.getId());
            //create a notificaiton with INFO status which says that bookname has been loaned to userid

            // Retrieve the user by the given user ID

            // Create the notification entity
            Notification notification = Notification.builder()
                    .message(reservation.getBook().getTitle() + " has been loaned.")
                    .seen(false)
                    .type(NotificationType.INFO)
                    .user(reservation.getUser())
                    .build();

            // Save the notification entity to the database
            notificationRepository.save(notification);
        }


        log.debug("exiting AdminServiceImpl.submitBook() service with return data: {}", submitBookDTO.toString());
        return  submitBookDTO;
    }

    @Override
    public List<ReservationDto> getUserReservation(long userId) throws ResourceNotFoundException {

        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new ResourceNotFoundException("user","id",userId);

        List<ReservationDto> reservationList = userRepository.findReservationHistory(userId)
                .stream()
                .map(reservation -> {
                    String base64Image = "";
                    try{
                        File imageFile = new File("E:/production/hexa/lms/lms/src/main/resources/static/"+reservation.getImgUrl());

                        FileInputStream fileInputStreamReader = new FileInputStream(imageFile);

                        byte[] imageData = new byte[(int) imageFile.length()];

                        fileInputStreamReader.read(imageData);

                        fileInputStreamReader.close();

                        base64Image = Base64.getEncoder().encodeToString(imageData);
                    } catch (Exception e){
                        log.info("error in finding image");
                    }

                    return ReservationDto.builder()
                        .imgUrl(base64Image)
                        .id(reservation.getId())
                        .bookId(reservation.getBookId())
                        .issueTimestamp(reservation.getIssueTimestamp())
                        .bookName(reservation.getBookName())
                        .build();
                })
                .collect(Collectors.toList());
        log.info("reservation list found");

        log.debug("exiting USerServiceImpl.getUserReservation() service with return data: {}", reservationList.toString());
        return reservationList;
    }

    @Override
    public UserDetailDto updateUserDetails(Long id, UserDetailDto userDetailDto) throws ResourceNotFoundException {
        log.debug("Entered BokServiceImpl.partialUpdate()  with arg: {} and {} ", id, userDetailDto.toString());
        User userEntity = userMapper.mapFrom(userDetailDto);


        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("user", "userid", id);
        }

        return user.map(existingUser -> {
            // Update existingBook with data from bookEntity
            Optional.ofNullable(userEntity.getFirstName()).ifPresent(existingUser::setFirstName);
            Optional.ofNullable(userEntity.getLastName()).ifPresent(existingUser::setLastName);
            Optional.ofNullable(userEntity.getContactNo()).ifPresent(existingUser::setContactNo);
            Optional.ofNullable(userEntity.getAddress()).ifPresent(existingUser::setAddress);
            Optional.ofNullable(userEntity.getGender()).ifPresent(existingUser::setGender);

            // Save the updated bookEntity
            User updatedUser = userRepository.save(existingUser);
            UserDetailDto userDetailDto1 = userMapper.mapTo(updatedUser);

            // Convert the updated BookEntity back to a BookDto
            log.debug("Exited BookServiceImpl.partialUpdate()  with return data: {} ", userDetailDto1.toString());
            return userDetailDto1;
        }).orElse(null);

    }

    @Override
    public void setOneNotificationSeen(long notificationId) {
        log.debug("Entered UserServiceImpl.setOneNotificationSeen()  with arg: {}  ", notificationId);
        notificationRepository.markNotificationAsSeen(notificationId);
        log.debug("Exited UserServiceImpl.setOneNotificationSeen()");
    }
    @Override
    public void setUserNotificationSeen(long userId) {
        log.debug("Entered UserServiceImpl.setUserNotificationSeen()  with arg: {}  ", userId);
        notificationRepository.markAllNotificationsAsSeenForUser(userId);
        log.debug("Exited UserServiceImpl.setUserNotificationSeen() ");
    }

}
