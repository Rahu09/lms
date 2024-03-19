package com.hexaware.lms.controller;

import com.hexaware.lms.dto.*;
import com.hexaware.lms.entity.Loan;
import com.hexaware.lms.entity.Reservation;
import com.hexaware.lms.exception.LoanLimitReachedException;
import com.hexaware.lms.exception.ResourceNotFoundException;

import com.hexaware.lms.exception.AmountInsufficientException;
import com.hexaware.lms.service.BookService;
import com.hexaware.lms.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@PreAuthorize("hasRole('USER')")
@Slf4j
public class UserController {
    private final UserService userService;


    //create new reservation
    @PostMapping(path = "/reservation/{userid}/{bookid}")
    public Reservation createReservation(@PathVariable("userid") @NotNull Long userid, @PathVariable("bookid") @NotNull Long bookid) throws ResourceNotFoundException {
        log.debug("Entered createReservation() controller.");
        log.info("Request recieved: api/v1/user/reservation/{userid}/{bookid}");
        Reservation createdReservation = userService.createReservation(userid,bookid);
        log.debug("Exited createReservation() controller.");
        return createdReservation;
    }

    //create new book loan
    @PostMapping(path = "/loan/{userid}/{bookid}")
    public Loan createLoan(@PathVariable("userid") @NotNull Long userid, @PathVariable("bookid") @NotNull Long bookid) throws ResourceNotFoundException, LoanLimitReachedException {
        log.debug("Entered createLoan() controller.");
        log.info("Request recieved: api/v1/user/loan/{userid}/{bookid}");
        Loan createdLoan = userService.createLoan(userid, bookid);
        log.debug("Exited createLoan() controller.");
        return createdLoan;
    }
    @GetMapping(path = "/userLoanHistory/{userId}")
    public ResponseEntity<List<UserLoanHistoryDTO>> userLoanHistory(
            @PathVariable("userId")@NotNull long userId
    ) throws ResourceNotFoundException {
        log.debug("entered userLoanHistory() controller");
        log.info("Request received: {} - {}", "userLoanHistory()", "/api/v1/user/userLoanHistory/{userId}");
        try{
            List<UserLoanHistoryDTO> userLoanHistoryList = userService.getUserLoanHistory(userId);

            log.debug("exiting userLoanHistory() controller with HttpStatus.OK");
            return new ResponseEntity<>(userLoanHistoryList,HttpStatus.OK);
        }catch (ResourceNotFoundException e){
            log.error("exiting userLoanHistory() controller with HttpStatus.NOT_FOUND and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("exiting userLoanHistory() controller with HttpStatus.INTERNAL_SERVER_ERROR and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping(path = "/userFine/{userId}")
    public ResponseEntity<List<fineDTO>> userFine(
            @PathVariable("userId")@NotNull long userId
    ) throws ResourceNotFoundException {
        log.debug("entered userFine() controller");
        log.info("Request received: {} - {}", "userFine()", "/api/v1/user/userFine/{userId}");
            List<fineDTO> userFineList = userService.getUserFine(userId);

            log.debug("exiting userFine() controller with HttpStatus.OK");
            return new ResponseEntity<>(userFineList,HttpStatus.OK);
    }

    @PutMapping(path = "/setStatusLost/{loanId}")
    public ResponseEntity setStatusLost(
            @PathVariable("loanId")@NotNull long loanId
    ) throws ResourceNotFoundException,IllegalArgumentException {
        log.debug("entered setStatusLost() controller");
        log.info("Request received: {} - {}", "setStatusLost()", "/api/v1/user/setStatusLost/{loanId}");
            userService.setStatusLost(loanId);

            log.debug("exiting setStatusLost() controller with HttpStatus.OK");
            return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping(path = "/submitBook/{loanId}")
    public ResponseEntity<SubmitBookDTO> submitBook(
            @PathVariable("loanId")@NotNull long loanId,
            @RequestParam(name = "fineAmount") long fineAmount
    ) throws ResourceNotFoundException, AmountInsufficientException {
        log.debug("entered submitBook() controller");
        log.info("Request received: {} - {}", "submitBook()", "/api/v1/user/submitBook/{loanId}");
            SubmitBookDTO submitData = userService.submitBook(loanId,fineAmount);

            log.debug("exiting submitBook() controller with HttpStatus.OK");
            return new ResponseEntity<>(submitData,HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping(path = "/userReservationHistory/{userId}")
    public ResponseEntity<List<ReservationDto>> userReservationHistory(
            @PathVariable("userId")@NotNull long userId
    ) throws ResourceNotFoundException {
        log.debug("entered userReservationHistory() controller");
        log.info("Request received: {} - {}", "userReservationHistory()", "/api/v1/user/userReservationHistory/{userId}");
            List<ReservationDto> userReservationHistoryList = userService.getUserReservation(userId);

            log.debug("exiting userReservationHistory() controller with HttpStatus.OK");
            return new ResponseEntity<>(userReservationHistoryList,HttpStatus.OK);

    }

    //update user from userid
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping(path = "/userdetails/{userId}")
    public ResponseEntity<UserDetailDto> updateUserDetails(@PathVariable("userId") @NotNull Long userId, @RequestBody UserDetailDto userDetailDto) throws ResourceNotFoundException {
        log.debug("Entered updateUserDetails() controller.");
        log.info("Request received: api/v1/user/userdetails/{userId}");

        // Call the service method to update user details
        UserDetailDto updatedUserDetailDto = userService.updateUserDetails(userId, userDetailDto);

        log.debug("Exited updateUserDetails() controller.");
        return new ResponseEntity<>(updatedUserDetailDto, HttpStatus.OK);
    }

    //set one notification as seen
    @PostMapping(path = "/notification/one-notification/{notificaitonId}")
    public ResponseEntity setOneNotificationSeen(
            @PathVariable("notificaitonId") @NotNull long notificationId
    ) throws ResourceNotFoundException{
        log.debug("entered setOneNotificationSeen() controller");

        userService.setOneNotificationSeen(notificationId);

        log.debug("exiting setOneNotificationSeen() controller with HttpStatus.OK");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //set all notification as seen
    @PostMapping(path = "/notification/all-notification/{userId}")
    public ResponseEntity setAllNotificationSeen(
            @PathVariable("userId") @NotNull long userId
    ) throws ResourceNotFoundException{
        log.debug("entered setAllNotificationSeen() controller");

        userService.setUserNotificationSeen(userId);

        log.debug("exiting setAllNotificationSeen() controller with HttpStatus.OK");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}