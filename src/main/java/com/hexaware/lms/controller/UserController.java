package com.hexaware.lms.controller;

import com.hexaware.lms.entity.Loan;
import com.hexaware.lms.entity.Reservation;
import com.hexaware.lms.exception.LoanLimitReachedException;
import com.hexaware.lms.exception.ResourceNotFoundException;

import com.hexaware.lms.dto.UserLoanHistoryDTO;
import com.hexaware.lms.dto.fineDTO;
import com.hexaware.lms.dto.SubmitBookDTO;
import com.hexaware.lms.exception.AmountInsufficientException;
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

    @GetMapping(path = "/userFine/{userId}")
    public ResponseEntity<List<fineDTO>> userFine(
            @PathVariable("userId")@NotNull long userId
    ) throws ResourceNotFoundException {
        log.debug("entered userFine() controller");
        log.info("Request received: {} - {}", "userFine()", "/api/v1/user/userFine/{userId}");
        try{
            List<fineDTO> userFineList = userService.getUserFine(userId);

            log.debug("exiting userFine() controller with HttpStatus.OK");
            return new ResponseEntity<>(userFineList,HttpStatus.OK);
        }catch (ResourceNotFoundException e){
            log.error("exiting userFine() controller with HttpStatus.NOT_FOUND and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            log.error("exiting userFine() controller with HttpStatus.INTERNAL_SERVER_ERROR and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/setStatusLost/{loanId}")
    public ResponseEntity setStatusLost(
            @PathVariable("loanId")@NotNull long loanId
    ) throws ResourceNotFoundException,IllegalArgumentException {
        log.debug("entered setStatusLost() controller");
        log.info("Request received: {} - {}", "setStatusLost()", "/api/v1/user/setStatusLost/{loanId}");
        try{
            userService.setStatusLost(loanId);

            log.debug("exiting setStatusLost() controller with HttpStatus.OK");
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (ResourceNotFoundException e){
            log.error("exiting setStatusLost() controller with HttpStatus.NOT_FOUND and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (IllegalArgumentException e){
            log.error("exiting setStatusLost() controller with HttpStatus.NOT_ACCEPTABLE and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        catch (Exception e){
            log.error("exiting setStatusLost() controller with HttpStatus.INTERNAL_SERVER_ERROR and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/submitBook/{loanId}")
    public ResponseEntity<SubmitBookDTO> submitBook(
            @PathVariable("loanId")@NotNull long loanId,
            @RequestParam(name = "fineAmount") long fineAmount
    ) throws ResourceNotFoundException, AmountInsufficientException {
        log.debug("entered submitBook() controller");
        log.info("Request received: {} - {}", "submitBook()", "/api/v1/user/submitBook/{loanId}");
        try{
            SubmitBookDTO submitData = userService.submitBook(loanId,fineAmount);

            log.debug("exiting submitBook() controller with HttpStatus.OK");
            return new ResponseEntity<>(submitData,HttpStatus.OK);
        }catch (ResourceNotFoundException e){
            log.error("exiting submitBook() controller with HttpStatus.NOT_FOUND and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (AmountInsufficientException e){
            log.error("exiting submitBook() controller with HttpStatus.PAYMENT_REQUIRED and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.PAYMENT_REQUIRED);
        }catch (IllegalArgumentException e){
            log.error("exiting submitBook() controller with HttpStatus.NOT_ACCEPTABLE and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        catch (Exception e){
            log.error("exiting submitBook() controller with HttpStatus.INTERNAL_SERVER_ERROR and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}