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
        try {
            adminService.deleteCategory(id);

            log.debug("exiting deleteCategory() controller with HttpStatus.NO_CONTENT");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e){
            log.error("exiting deleteCategory() controller with HttpStatus.NOT_FOUND and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("exiting deleteCategory() controller with HttpStatus.INTERNAL_SERVER_ERROR and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/returnRequest/{id}")
    public ResponseEntity<NotificationDTO> returnRequest(
            @RequestBody NotificationDTO notificationDTO,
            @PathVariable("id") @NotNull long id
    ) throws ResourceNotFoundException {
        log.debug("entered returnRequest() controller");
        log.info("Request received: {} - {}", "returnRequest()", "/api/v1/admin/returnRequest/{id}");
        try{
            NotificationDTO savedDto = adminService.sendReturnRequest(notificationDTO, id);

            log.debug("exiting returnRequest() controller with HttpStatus.OK");
            return new ResponseEntity(savedDto,HttpStatus.OK);
        } catch (ResourceNotFoundException e){
            log.error("exiting returnRequest() controller with HttpStatus.NOT_FOUND and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("exiting returnRequest() controller with HttpStatus.INTERNAL_SERVER_ERROR and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/bookLoanHistory/{bookId}")
    public ResponseEntity<List<BookLoanHistoryDTO>> bookLoanHistory(
            @PathVariable("bookId")@NotNull long bookId
    ) throws ResourceNotFoundException {

        log.debug("entered bookLoanHistory() controller");
        log.info("Request received: {} - {}", "bookLoanHistory()", "/api/v1/admin/bookLoanHistory/{bookId}");
        try{
            List<BookLoanHistoryDTO> bookLoanHistoryList = adminService.getBookLoanHistory(bookId);

            log.debug("exiting bookLoanHistory() controller with HttpStatus.OK");
            return new ResponseEntity<>(bookLoanHistoryList,HttpStatus.OK);
        }catch (ResourceNotFoundException e){
            log.error("exiting bookLoanHistory() controller with HttpStatus.NOT_FOUND and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("exiting bookLoanHistory() controller with HttpStatus.INTERNAL_SERVER_ERROR and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/bookReservationHistory/{bookId}")
    public ResponseEntity<List<BookReservationHistoryDTO>> bookReservationHistory(
            @PathVariable("bookId") @NotNull long bookId
    ) throws ResourceNotFoundException {

        log.debug("entered bookReservationHistory() controller");
        log.info("Request received: {} - {}", "bookReservationHistory()", "/api/v1/admin/bookReservationHistory/{bookId}");
        try{
            List<BookReservationHistoryDTO> bookLoanHistoryList = adminService.getBookReservationHistory(bookId);

            log.debug("exiting bookReservationHistory() controller with HttpStatus.OK");
            return new ResponseEntity<>(bookLoanHistoryList,HttpStatus.OK);
        }catch (ResourceNotFoundException e){
            log.error("exiting bookReservationHistory() controller with HttpStatus.NOT_FOUND and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("exiting bookReservationHistory() controller with HttpStatus.INTERNAL_SERVER_ERROR and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/userLoanHistory/{userId}")
    public ResponseEntity<List<UserLoanHistoryDTO>> userLoanHistory(
            @PathVariable("userId") @NotNull long userId
    ) throws ResourceNotFoundException {
        log.debug("entered userLoanHistory() controller");
        log.info("Request received: {} - {}", "userLoanHistory()", "/api/v1/admin/userLoanHistory/{userId}");
        try{
            List<UserLoanHistoryDTO> userLoanHistoryList = adminService.getUserLoanHistory(userId);

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
            @PathVariable("userId") @NotNull long userId
    ) throws ResourceNotFoundException {
        log.debug("entered userFine() controller");
        log.info("Request received: {} - {}", "userFine()", "/api/v1/admin/userFine/{userId}");
        try{
            List<fineDTO> userFineList = adminService.getUserFine(userId);

            log.debug("exiting userFine() controller with HttpStatus.OK");
            return new ResponseEntity<>(userFineList,HttpStatus.OK);
        }catch (ResourceNotFoundException e){
            log.error("exiting userFine() controller with HttpStatus.NOT_FOUND and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("exiting userFine() controller with HttpStatus.INTERNAL_SERVER_ERROR and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/totalFine")
    public ResponseEntity<List<fineDTO>> totalFine(
    ) throws ResourceNotFoundException {
        log.debug("entered totalFine() controller");
        log.info("Request received: {} - {}", "totalFine()", "/api/v1/admin/totalFine");
        try{
            List<fineDTO> userFineList = adminService.getTotalFine();

            log.debug("exiting totalFine() controller with HttpStatus.OK");
            return new ResponseEntity<>(userFineList,HttpStatus.OK);
        }catch (ResourceNotFoundException e){
            log.error("exiting totalFine() controller with HttpStatus.NOT_FOUND and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("exiting totalFine() controller with HttpStatus.INTERNAL_SERVER_ERROR and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}