package com.hexaware.lms.service;

import com.hexaware.lms.dto.*;
import com.hexaware.lms.entity.Loan;
import com.hexaware.lms.entity.Reservation;
import com.hexaware.lms.exception.LoanLimitReachedException;
import com.hexaware.lms.exception.ResourceNotFoundException;


import com.hexaware.lms.exception.AmountInsufficientException;
import com.hexaware.lms.exception.ResourceNotFoundException;

import java.util.List;

public interface UserService {
    public Reservation createReservation(Long userid, Long bookid) throws ResourceNotFoundException;

    public Loan createLoan(Long userid, Long bookid) throws ResourceNotFoundException, LoanLimitReachedException;

    List<UserLoanHistoryDTO> getUserLoanHistory(long userId) throws ResourceNotFoundException;

    List<fineDTO> getUserFine(long userId) throws ResourceNotFoundException;

    void setStatusLost(long loanId) throws ResourceNotFoundException,IllegalArgumentException;

    SubmitBookDTO submitBook(long loanId, long fineAmount) throws ResourceNotFoundException, AmountInsufficientException,IllegalArgumentException;

    List<ReservationDto> getUserReservation(long userId) throws ResourceNotFoundException;

    UserDetailDto updateUserDetails(Long userId, UserDetailDto userDetailDto) throws ResourceNotFoundException;

    void setOneNotificationSeen(long notificationId);

    void setUserNotificationSeen(long userId);
}
