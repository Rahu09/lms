package com.hexaware.lms.controller;

import com.hexaware.lms.dto.*;
import com.hexaware.lms.exception.ResourceNotFoundException;
import com.hexaware.lms.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin")
@Slf4j
public class AdminController {
    private final AdminService adminService;
        @Operation(
            description = "getCategory - Get endpoint for admin",
            summary = "This endpoint is used to ge all the available categories present.",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }

    )
    @GetMapping(path = "/getCategory")
    public ResponseEntity<List<CategoryDTO>> getCategory() throws FileNotFoundException {
        log.debug("entered /getCategory() controller");
        log.info("Request received: {} - {}", "getCategory()", "/api/v1/admin/getCategory");

        List<CategoryDTO> response = adminService.findAll();

        log.debug("exiting /getCategory() controller with return result response: "+response.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/addCategory")
    public ResponseEntity<CategoryDTO> addCategory(
            @RequestBody AddCategoryRequestBody request
    ){
        CategoryDTO response = adminService.addCategory(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @DeleteMapping(path = "/deleteCategory/{id}")
    public ResponseEntity deleteCategory(
            @PathVariable("id") long id
    ) throws ResourceNotFoundException {
        log.debug("entered deleteCategory() controller");
        log.info("Request received: {} - {}", "getCategory()", "/api/v1/admin/deleteCategory/{id}");
        
            adminService.deleteCategory(id);

            log.debug("exiting deleteCategory() controller with HttpStatus.NO_CONTENT");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      
    }

    @PatchMapping(path="/updateBook/{id}")
    public ResponseEntity<BookDto> partialUpdate(@PathVariable("id") @NotNull Long id, @NotNull @RequestBody BookDto bookDto) throws ResourceNotFoundException{

        log.debug("Entered partialUpdatebook() controller.");
        log.info("Request recieved: api/v1/book/books/{id}");
        BookDto updatedbookDto = adminService.partialUpdate(id, bookDto);
        log.debug("Exited partialUpdatebook() controller with HttpStatus.OK.");
        return new ResponseEntity<>(updatedbookDto, HttpStatus.OK);
    }

    @DeleteMapping(path = "/deleteBook/{id}")
    public ResponseEntity deleteBook(@PathVariable("id") @NotNull Long id) throws ResourceNotFoundException{
        log.debug("Entered deletebook() controller.");
        log.info("Request recieved: api/v1/book/deletebook/{id}");

        adminService.delete(id);
        log.debug("Exited deletebook() controller with HttpStatus.NO_CONTENT.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/returnRequest/{id}")
    public ResponseEntity<NotificationDTO> returnRequest(
            @RequestBody NotificationDTO notificationDTO,
            @PathVariable("id") @NotNull long id
    ) throws ResourceNotFoundException {
        log.debug("entered returnRequest() controller");
        log.info("Request received: {} - {}", "returnRequest()", "/api/v1/admin/returnRequest/{id}");
            NotificationDTO savedDto = adminService.sendReturnRequest(notificationDTO, id);

            log.debug("exiting returnRequest() controller with HttpStatus.OK");
            return new ResponseEntity<NotificationDTO>(savedDto,HttpStatus.OK);
        
    }

    @GetMapping(path = "/bookLoanHistory/{bookId}")
    public ResponseEntity<List<BookLoanHistoryDTO>> bookLoanHistory(
            @PathVariable("bookId")@NotNull long bookId
    ) throws ResourceNotFoundException {

        log.debug("entered bookLoanHistory() controller");
        log.info("Request received: {} - {}", "bookLoanHistory()", "/api/v1/admin/bookLoanHistory/{bookId}");
            List<BookLoanHistoryDTO> bookLoanHistoryList = adminService.getBookLoanHistory(bookId);

            log.debug("exiting bookLoanHistory() controller with HttpStatus.OK");
            return new ResponseEntity<>(bookLoanHistoryList,HttpStatus.OK);
        
    }

    @GetMapping(path = "/bookReservationHistory/{bookId}")
    public ResponseEntity<List<BookReservationHistoryDTO>> bookReservationHistory(
            @PathVariable("bookId") @NotNull long bookId
    ) throws ResourceNotFoundException {

        log.debug("entered bookReservationHistory() controller");
        log.info("Request received: {} - {}", "bookReservationHistory()", "/api/v1/admin/bookReservationHistory/{bookId}");
            List<BookReservationHistoryDTO> bookLoanHistoryList = adminService.getBookReservationHistory(bookId);

            log.debug("exiting bookReservationHistory() controller with HttpStatus.OK");
            return new ResponseEntity<>(bookLoanHistoryList,HttpStatus.OK);
       
    }

    @GetMapping(path = "/userLoanHistory/{userId}")
    public ResponseEntity<List<UserLoanHistoryDTO>> userLoanHistory(
            @PathVariable("userId") @NotNull long userId
    ) throws ResourceNotFoundException {
        log.debug("entered userLoanHistory() controller");
        log.info("Request received: {} - {}", "userLoanHistory()", "/api/v1/admin/userLoanHistory/{userId}");
       
            List<UserLoanHistoryDTO> userLoanHistoryList = adminService.getUserLoanHistory(userId);

            log.debug("exiting userLoanHistory() controller with HttpStatus.OK");
            return new ResponseEntity<>(userLoanHistoryList,HttpStatus.OK);
        
    }

    @GetMapping(path = "/userFine/{userId}")
    public ResponseEntity<List<fineDTO>> userFine(
            @PathVariable("userId") @NotNull long userId
    ) throws ResourceNotFoundException {
        log.debug("entered userFine() controller");
        log.info("Request received: {} - {}", "userFine()", "/api/v1/admin/userFine/{userId}");
       
            List<fineDTO> userFineList = adminService.getUserFine(userId);

            log.debug("exiting userFine() controller with HttpStatus.OK");
            return new ResponseEntity<>(userFineList,HttpStatus.OK);
       
    }

    @GetMapping(path = "/totalFine")
    public ResponseEntity<List<fineDTO>> totalFine(
    ) throws ResourceNotFoundException {
        log.debug("entered totalFine() controller");
        log.info("Request received: {} - {}", "totalFine()", "/api/v1/admin/totalFine");
        
            List<fineDTO> userFineList = adminService.getTotalFine();

            log.debug("exiting totalFine() controller with HttpStatus.OK");
            return new ResponseEntity<>(userFineList,HttpStatus.OK);
       
    }

    @GetMapping(path = "/loanwarncount")
    public ResponseEntity<Integer> loanWarnCount(
    ) {
        log.debug("entered loanWarnCount() controller");
        log.info("Request received: {} - {}", "loanWarnCount()", "/api/v1/admin/loanwarncount");
        
            Integer loanwarncount = adminService.getLoanWarningCount();

            log.debug("exiting loanWarnCount() controller with HttpStatus.OK");
            return new ResponseEntity<>(loanwarncount, HttpStatus.OK);
       
    }

    @GetMapping(path = "/lateloan")
    public ResponseEntity<List<LoanDto>> lateLoan(
    ) {
        log.debug("entered lateLoan() controller");
        log.info("Request received: {} - {}", "lateLoan()", "/api/v1/admin/lateloan");
        
            List<LoanDto> lateLoan = adminService.getLateLoan();

            log.debug("exiting lateLoan() controller with HttpStatus.OK");
            return new ResponseEntity<>(lateLoan, HttpStatus.OK);
       
    }


    //return book request - set status as late for late books(bookid, userid), create a notification for userid and bookid(get bookname from this)
    @PostMapping("/alert-book-request")
    public ResponseEntity<String> createAlertNotification() {
        adminService.createAlertRequest();  //sends notification as well as sets loan status to late
        return ResponseEntity.ok("Alert notification created successfully.");
    }

    @PostMapping("/remind-book-request")
    public ResponseEntity<String> createReminderNotification() {
        adminService.createReminderRequest();   //sends notificaiton
        return ResponseEntity.ok("Reminder notification created successfully.");
    }
}