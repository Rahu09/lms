package com.hexaware.lms.controller;

import com.hexaware.lms.dto.BookDto;
import com.hexaware.lms.dto.BookFilterDto;
import com.hexaware.lms.dto.CategoryDTO;
import com.hexaware.lms.entity.Book;
import com.hexaware.lms.exception.ResourceNotFoundException;
import com.hexaware.lms.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "User")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/book")
public class BookController {
    private final BookService bookService;

    //create book
    @PostMapping(path= "/addbook")
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookDto bookDto) throws IOException {
        log.debug("Entered createbook() controller.");
        log.info("Request recieved: /api/v1/book/addbook");
        BookDto savedBookDto = bookService.save(bookDto);
        log.debug("Exiting createBook() controller with HttpStatus.CREATED.");
        return new ResponseEntity<>(savedBookDto, HttpStatus.CREATED) ;
    }

    @GetMapping(path="/booksName")
    public List<String> getAllBooksName(){
        log.debug("Entered getAllBooksName() controller.");
        log.info("Request recieved: /api/v1/book/getAllBooksName");
        List<String > books = bookService.getAllBooksName();
        log.debug("Exited getAllBooksName() controller.");
        return books;
    }

    @GetMapping(path="/books")
    public Page<BookDto> getAllBooks(Pageable pageable){
        log.debug("Entered getallbooks() controller.");
        log.info("Request recieved: /api/v1/book/books");
        Page<BookDto> books = bookService.findAll(pageable);
        log.debug("Exited getallbooks() controller.");
        return books;
    }

    @PostMapping(path="/bookFilter")
    public Page<BookDto> getBookFilter(
            Pageable pageable,
            @RequestBody BookFilterDto bookFilterDto
    ){
        log.debug("Entered getBookFilter() controller.");
        log.info("Request recieved: /api/v1/book/bookFilter");
        Page<BookDto> books = bookService.bookFilter(bookFilterDto,pageable);
        log.debug("Exited getBookFilter() controller.");
        return books;
    }


    @Operation(
            description = "Get endpoint for user",
            summary = "This is a summary for admin get endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Internal server Error",
                            responseCode = "500"
                    )

            }

    )
    //get book by id
    @GetMapping(path = "/books/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable("id") @NotNull Long id) throws ResourceNotFoundException{

        log.debug("Entered getbookbyid controller.");
        log.info("Request recieved: api/v1/book/books/{id}");
        try{
            BookDto bookDto = bookService.findOne(id);
            log.debug("Exited getbooksbyid() controller with HttpStatus.OK.");
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        }
        catch (ResourceNotFoundException e)
        {
            log.debug("Exited getbooksbyid() controller with HttpStatus.NOT_FOUND.and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e)
        {
            log.debug("Exited getbooksbyid() controller with HttpStatus.INTERNAL_SERVER_ERROR.and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //book full update
//    @PutMapping(path = "/updateBook/{id}")
//    public ResponseEntity<BookDto> fullUpdateBook(@PathVariable("id") @NotNull Long id, @Valid @RequestBody BookDto bookDto) {
//        log.debug("Entered fullupdatebook() controller.");
//        log.info("Request recieved: api/v1/book/books/{id}");
//        try{
//            BookDto bookDto1 = bookService.fullUpdate(bookDto,id);
//            log.debug("Exited fullupdatebook() controller with HttpStatus.OK.");
//            return new ResponseEntity<>(bookDto1, HttpStatus.OK);
//        }
//        catch (Exception e)
//        {
//            log.debug("Exited fullupdatebook() controller with HttpStatus.INTERNAL_SERVER_ERROR.and exception: \n"+e.toString());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }

    //book partial update
//    @PatchMapping(path="/updateBook/{id}")
//    public ResponseEntity<BookDto> partialUpdate(@PathVariable("id") @NotNull Long id, @NotNull @RequestBody BookDto bookDto) throws ResourceNotFoundException{
//
//        log.debug("Entered partialUpdatebook() controller.");
//        log.info("Request recieved: api/v1/book/books/{id}");
//        BookDto updatedbookDto = bookService.partialUpdate(id, bookDto);
//        log.debug("Exited partialUpdatebook() controller with HttpStatus.OK.");
//        return new ResponseEntity<>(updatedbookDto, HttpStatus.OK);
//    }

    //delete by id
//    @DeleteMapping(path = "/deletebook/{id}")
//    public ResponseEntity deleteBook(@PathVariable("id") @NotNull Long id) throws ResourceNotFoundException{
//        log.debug("Entered deletebook() controller.");
//        log.info("Request recieved: api/v1/book/deletebook/{id}");
//
//        bookService.delete(id);
//        log.debug("Exited deletebook() controller with HttpStatus.NO_CONTENT.");
//        return new ResponseEntity(HttpStatus.NO_CONTENT);
//    }

    //search bar autosuggestions
    @GetMapping(path = "/search/{searchquery}")
    public ResponseEntity<List<BookDto>> getBookSearchBar(@PathVariable("searchquery") @NotEmpty String search) throws ResourceNotFoundException{
        log.debug("Entered getBookSearchBar() controller.");
        log.info("Request recieved: api/v1/book/search/{searchquery}");
        List<BookDto> books = bookService.searchBarBook(search);
        log.debug("Exited getBookSearchBar() controller with HttpStatus.OK.");
        return new ResponseEntity<>(books, HttpStatus.OK);

    }

    //find by authorname
    @GetMapping(path = "/search/authorname/{search}")
    public ResponseEntity<List<Book>> getBookByAuthor(@PathVariable("search") @NotEmpty String search) throws ResourceNotFoundException
    {
        log.debug("Entered getBookByAuthor() controller.");
        log.info("Request recieved: api/v1/book/search/authorname/{search}");
        Optional<List<Book>> books = null;
        try
        {
            books = bookService.searchByAuthor(search);
            log.debug("Exited getBookByAuthor() controller with HttpStatus.OK).");
            return new ResponseEntity<>(books.get(), HttpStatus.OK);
        }
        catch (ResourceNotFoundException e)
        {
            log.debug("Exited getBookByAuthor() controller with HttpStatus.NOT_FOUND and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e)
        {
            log.debug("Exited getBookByAuthor() controller with HttpStatus.INTERNAL_SERVER_ERROR)and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //find by language
    @GetMapping(path = "/search/language/{search}")
    public ResponseEntity<List<Book>> getBookByLanguage(@PathVariable("search") @NotEmpty String search) throws ResourceNotFoundException {
        log.debug("Entered getBookByLanguage() controller.");
        log.info("Request recieved: api/v1/book/search/language/{search}");

        Optional<List<Book>> books = null;
        try
        {
            books = bookService.searchByLanguage(search);
            log.debug("Exited getBookByLanguage() controller with HttpStatus.OK.");
            return new ResponseEntity<>(books.get(), HttpStatus.OK);
        }
        catch (ResourceNotFoundException e)
        {
            log.debug("Exited getBookByLanguage() controller with HttpStatus.NOT_FOUND).and exception: \n"+e.toString() );
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e)
        {
            log.debug("Exited getBookByLanguage() controller with HttpStatus.INTERNAL_SERVER_ERROR) and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //find by category
    @GetMapping(path = "/search/category/{search}")
    public ResponseEntity<List<Book>> getBookByCategory(@PathVariable("search") @NotEmpty String search) throws ResourceNotFoundException {

        log.debug("Entered getBookByCategory() controller.");
        log.info("Request recieved: api/v1/book/search/category/{search}");
        Optional<List<Book>> books = null;
        try
        {
            books = bookService.findByCategory(search);
            log.debug("Exited getBookByCategory() controller with HttpStatus.OK.");
            return new ResponseEntity<>(books.get(), HttpStatus.OK);
        }
        catch (ResourceNotFoundException e)
        {
            log.debug("Exited getBookByCategory() controller with HttpStatus.NOT_FOUND and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e)
        {
            log.debug("Exited getBookByCategory() controller with HttpStatus.INTERNAL_SERVER_ERROR) and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    //get category by bookid
    @GetMapping(path = "/bookcategory/{id}")
    public ResponseEntity<List<String>> getCategoryByBook(@PathVariable("id") @NotNull Long id) throws ResourceNotFoundException {

        log.debug("Entered getCategoryByBook controller.");
        log.info("Request recieved: api/v1/book/books/{id}");
        Optional<List<String>> category;

        category = bookService.findCategory(id);
        log.debug("Exited getCategoryByBook() controller with HttpStatus.OK.");
        return new ResponseEntity<>(category.get(), HttpStatus.OK);

    }

    //get loan count by bookid
    @GetMapping(path = "/bookloancount/{id}")
    public ResponseEntity<Integer> getLoanCountByBook(@PathVariable("id") @NotNull Long id) throws ResourceNotFoundException {

        log.debug("Entered getLoanCountByBook controller.");
        log.info("Request recieved: api/v1/book/bookloancount/{id}");

        Integer loanCount = bookService.getLoanCount(id);

        log.debug("Exited getLoanCountByBook() controller with HttpStatus.OK.");
        return new ResponseEntity<>(loanCount, HttpStatus.OK);

    }

    //get reservation count by bookid
    @GetMapping(path = "/bookreservationcount/{id}")
    public ResponseEntity<Integer> getReservationCountByBook(@PathVariable("id") @NotNull Long id) throws ResourceNotFoundException {

        log.debug("Entered getReservationCountByBook controller.");
        log.info("Request recieved: api/v1/book/bookreservationcount/{id}");

        Integer reservationCount = bookService.getReservationCount(id);

        log.debug("Exited getReservationCountByBook() controller with HttpStatus.OK.");
        return new ResponseEntity<>(reservationCount, HttpStatus.OK);

    }

    //get no of books loaned by a user
    @PostMapping(path = "/userbookloancount")
    public ResponseEntity<Integer> getNoOfBooksLoanByEmail(@RequestBody Map<String, String> requestBody) {
        String userEmail = requestBody.get("email");

        log.debug("Entered getNoOfBooksLoanByEmail controller.");
        log.info("Request received: POST api/v1/book/userbookloancount");

        Integer noOfBooksLoan = bookService.findNoOfBooksLoanByEmail(userEmail);

        log.debug("Exited getNoOfBooksLoanByEmail() controller with HttpStatus.OK.");
        return ResponseEntity.ok(noOfBooksLoan);
    }

    //borrow/loan book with userid and bookid
    @PostMapping("/borrow/{userId}/{bookId}")
    public ResponseEntity borrowBook(@PathVariable Long userId, @PathVariable Long bookId) {
        log.debug("Entered borrowBook controller.");
        log.info("Request received: POST /api/v1/loan/borrow");

        bookService.borrowBook(userId, bookId);

        log.debug("Exited borrowBook controller with HttpStatus.CREATED.");
        return ResponseEntity.ok().build();
    }

    //user reserve book
    @PostMapping("/reserve/{userId}/{bookId}")
    public ResponseEntity reserveBook(@PathVariable Long userId, @PathVariable Long bookId) {
        log.debug("Entered reserveBook controller.");
        log.info("Request received: POST /api/v1/reservation/reserve");

        bookService.reserveBook(userId, bookId);

        log.debug("Exited reserveBook controller with HttpStatus.CREATED.");
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/getCategory")
    public ResponseEntity<List<String>> getCategory() throws FileNotFoundException {
        log.debug("entered /getCategory() controller");
        log.info("Request received: {} - {}", "getCategory()", "/api/v1/admin/getCategory");

        List<String> response = bookService.findAllCategory();

        log.debug("exiting /getCategory() controller with return result response: "+response.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //get authorname list and language list
        @GetMapping("/unique-authors")
    public ResponseEntity<List<String>> getAllUniqueAuthorNames() {
        List<String> uniqueAuthorNames = bookService.getAllUniqueAuthorNames();
        return ResponseEntity.ok(uniqueAuthorNames);
    }

    @GetMapping("/unique-languages")
    public ResponseEntity<List<String>> getAllUniqueLanguages() {
        List<String> uniqueLanguages = bookService.getAllUniqueLanguages();
        return ResponseEntity.ok(uniqueLanguages);
    }

}
