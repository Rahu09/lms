package com.hexaware.lms.service;

import com.hexaware.lms.dto.*;
import com.hexaware.lms.exception.ResourceNotFoundException;

import java.util.List;

public interface AdminService {
    List<CategoryDTO> findAll();

    CategoryDTO addCategory(AddCategoryRequestBody request);

    void deleteCategory(long id) throws ResourceNotFoundException;


    NotificationDTO sendReturnRequest(NotificationDTO notificationDTO, long id) throws ResourceNotFoundException;

    List<BookLoanHistoryDTO> getBookLoanHistory(long bookId) throws ResourceNotFoundException;

    List<BookReservationHistoryDTO> getBookReservationHistory(long bookId) throws ResourceNotFoundException;

    List<UserLoanHistoryDTO> getUserLoanHistory(long userId) throws ResourceNotFoundException;

    List<fineDTO> getUserFine(long userId) throws ResourceNotFoundException;

    List<fineDTO> getTotalFine() throws ResourceNotFoundException;

    Integer getLoanWarningCount();

    List<LoanDto> getLateLoan();

    void createAlertRequest();

    void createReminderRequest();

    BookDto partialUpdate(Long id, BookDto bookDto) throws ResourceNotFoundException;

    void delete(Long id) throws ResourceNotFoundException;
}
