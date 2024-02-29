package com.hexaware.lms.controller;

import com.hexaware.lms.dto.BookDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@Tag(name = "User")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/book")
@CrossOrigin("*")
public class BookController {
    private final BookService bookService;

    //create book
    @PostMapping(path= "/addbook")
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookDto bookDto)  {
        log.debug("Entered createbook() controller.");
        log.info("Request recieved: /api/v1/book/addbook");
        BookDto savedBookDto = bookService.save(bookDto);
        log.debug("Exiting createBook() controller with HttpStatus.CREATED.");
        return new ResponseEntity<>(savedBookDto, HttpStatus.CREATED) ;
    }

    //get all books
    @GetMapping(path="/books")
    public List<BookDto> getAllBooks(){
        log.debug("Entered getallbooks() controller.");
        log.info("Request recieved: /api/v1/book/books");
        List<BookDto> books = bookService.findAll();
        log.debug("Exited getallbooks() controller.");
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
    @PutMapping(path = "/books/{id}")
    public ResponseEntity<BookDto> fullUpdateBook(@PathVariable("id") @NotNull Long id, @Valid @RequestBody BookDto bookDto) {
        log.debug("Entered fullupdatebook() controller.");
        log.info("Request recieved: api/v1/book/books/{id}");
        try{
            BookDto bookDto1 = bookService.fullUpdate(bookDto,id);
            log.debug("Exited fullupdatebook() controller with HttpStatus.OK.");
            return new ResponseEntity<>(bookDto1, HttpStatus.OK);
        }
        catch (Exception e)
        {
            log.debug("Exited fullupdatebook() controller with HttpStatus.INTERNAL_SERVER_ERROR.and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    //book partial update
    @PatchMapping(path="/books/{id}")
    public ResponseEntity<BookDto> partialUpdate(@PathVariable("id") @NotNull Long id, @NotNull @RequestBody BookDto bookDto) throws ResourceNotFoundException{

        log.debug("Entered partialUpdatebook() controller.");
        log.info("Request recieved: api/v1/book/books/{id}");
        try{
            BookDto updatedbookDto = bookService.partialUpdate(id, bookDto);
            log.debug("Exited partialUpdatebook() controller with HttpStatus.OK.");
            return new ResponseEntity<>(updatedbookDto, HttpStatus.OK);
        }
        catch (ResourceNotFoundException e)
        {
            log.debug("Exited partialUpdatebook()() controller with HttpStatus.NOT_FOUND.and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e)
        {
            log.debug("Exited partialUpdatebook() controller with HttpStatus.INTERNAL_SERVER_ERROR.and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    //delete by id
    @DeleteMapping(path = "/deletebook/{id}")
    public ResponseEntity deleteBook(@PathVariable("id") @NotNull Long id) throws ResourceNotFoundException{
        log.debug("Entered deletebook() controller.");
        log.info("Request recieved: api/v1/book/deletebook/{id}");
        try{
            bookService.delete(id);
            log.debug("Exited deletebook() controller with HttpStatus.NO_CONTENT.");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        catch (ResourceNotFoundException e)
        {
            log.debug("Exited deletebook() controller with HttpStatus.NOT_FOUND.and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e)
        {
            log.debug("Exited deletebook() controller with HttpStatus.INTERNAL_SERVER_ERROR.and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //search bar autosuggestions
    @GetMapping(path = "/search/{searchquery}")
    public ResponseEntity<List<Book>> getBookSearchBar(@PathVariable("searchquery") @NotEmpty String search) throws ResourceNotFoundException{
        log.debug("Entered getBookSearchBar() controller.");
        log.info("Request recieved: api/v1/book/search/{searchquery}");
        Optional<List<Book>> books= null;

        try
        {
            books = bookService.searchBarBook(search);
            log.debug("Exited getBookSearchBar() controller with HttpStatus.OK.");
            return new ResponseEntity<>(books.get(), HttpStatus.OK);
        }
        catch (ResourceNotFoundException e)
        {
            log.debug("Exited getBookSearchBar() controller with HttpStatus.NOT_FOUND.and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e)
        {
            log.debug("Exited getBookSearchBar() controller with HttpStatus.INTERNAL_SERVER_ERROR.and exception: \n"+e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

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


}
