package com.hexaware.lms;

import com.hexaware.lms.entity.*;
import com.hexaware.lms.repository.*;
import com.hexaware.lms.utils.Gender;
import com.hexaware.lms.utils.LoanStatus;
import com.hexaware.lms.utils.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@SpringBootTest
@Slf4j
public class TestToInsertData {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationRepository authenticationRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookCategoryMapperRepository bookCategoryMapperRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
//    @Disabled
    public void insertUserTest(){
        var user1 = User.builder()
                .id(1L)
                .firstName("rahul")
                .lastName("tiwari")
                .address("hig")
                .contactNo("9999999999")
                .gender(Gender.MALE)
                .email("tiwarirahul0809@gmail.com")
                .noOfBooksLoan(0)
                .image("")
                .build();

        User savedUser1 = userRepository.save(user1);

        var auth1 = Authentication.builder()
                .email("tiwarirahul0809@gmail.com")
                .password(passwordEncoder.encode("root"))
                .role(Role.ADMIN)
                .user(savedUser1)
                .build();

        authenticationRepository.save(auth1);

        //

        var user2 = User.builder()
                .id(2L)
                .firstName("John")
                .lastName("Doe")
                .address("123 Main St")
                .contactNo("1234567890")
                .gender(Gender.MALE)
                .email("john.doe@example.com")
                .noOfBooksLoan(0)
                .image("")
                .build();

        User savedUser2 = userRepository.save(user2);

        var auth2 = Authentication.builder()
                .email("john.doe@example.com")
                .password(passwordEncoder.encode("root"))
                .role(Role.ADMIN)
                .user(savedUser2)
                .build();

        authenticationRepository.save(auth2);

//        Jane Smith
        var user3 = User.builder()
                .id(3L)
                .firstName("Jane")
                .lastName("Smith")
                .address("456 Oak Ave")
                .contactNo("9876543210")
                .gender(Gender.FEMALE)
                .email("jane.smith@example.com")
                .noOfBooksLoan(3)
                .image("")
                .build();

        User savedUser3 = userRepository.save(user3);

        var auth3 = Authentication.builder()
                .email("jane.smith@example.com")
                .password(passwordEncoder.encode("root"))
                .role(Role.USER)
                .user(savedUser3)
                .build();

        authenticationRepository.save(auth3);

        //
        var user4 = User.builder()
                .id(4L)
                .firstName("Bob")
                .lastName("Jones")
                .address("789 Pine Rd")
                .contactNo("5555555555")
                .gender(Gender.MALE)
                .email("bob.jones@example.com")
                .noOfBooksLoan(2)
                .image("")
                .build();

        User savedUser4 = userRepository.save(user4);

        var auth4 = Authentication.builder()
                .email("bob.jones@example.com")
                .password(passwordEncoder.encode("root"))
                .role(Role.USER)
                .user(savedUser4)
                .build();

        authenticationRepository.save(auth4);

        //
        var user5 = User.builder()
                .id(5L)
                .firstName("Alice")
                .lastName("Jenkins")
                .address("101 Elm St")
                .contactNo("3333333333")
                .gender(Gender.FEMALE)
                .email("alice.jenkins@example.com")
                .noOfBooksLoan(1)
                .image("")
                .build();

        User savedUser5 = userRepository.save(user5);

        var auth5 = Authentication.builder()
                .email("alice.jenkins@example.com")
                .password(passwordEncoder.encode("root"))
                .role(Role.USER)
                .user(savedUser5)
                .build();

        authenticationRepository.save(auth5);

        //
        var user6 = User.builder()
                .id(6L)
                .firstName("Sam")
                .lastName("Wilson")
                .address("222 Maple Ave")
                .contactNo("7777777777")
                .gender(Gender.MALE)
                .email("sam.wilson@example.com")
                .noOfBooksLoan(1)
                .image("")
                .build();

        User savedUser6 = userRepository.save(user6);

        var auth6 = Authentication.builder()
                .email("sam.wilson@example.com")
                .password(passwordEncoder.encode("root"))
                .role(Role.USER)
                .user(savedUser6)
                .build();

        authenticationRepository.save(auth6);

        //
        var user7 = User.builder()
                .id(7L)
                .firstName("Emma")
                .lastName("White")
                .address("444 Birch Blvd")
                .contactNo("8888888888")
                .gender(Gender.FEMALE)
                .email("emma.white@example.com")
                .noOfBooksLoan(1)
                .image("")
                .build();

        User savedUser7 = userRepository.save(user7);

        var auth7 = Authentication.builder()
                .email("emma.white@example.com")
                .password(passwordEncoder.encode("root"))
                .role(Role.USER)
                .user(savedUser7)
                .build();

        authenticationRepository.save(auth7);

        //
        var user8 = User.builder()
                .id(8L)
                .firstName("Michael")
                .lastName("Brown")
                .address("777 Cedar Ln")
                .contactNo("6666666666")
                .gender(Gender.MALE)
                .email("michael.brown@example.com")
                .noOfBooksLoan(1)
                .image("")
                .build();

        User savedUser8 = userRepository.save(user8);

        var auth8 = Authentication.builder()
                .email("michael.brown@example.com")
                .password(passwordEncoder.encode("root"))
                .role(Role.USER)
                .user(savedUser8)
                .build();

        authenticationRepository.save(auth8);

        //
        var user9 = User.builder()
                .id(9L)
                .firstName("Lisa")
                .lastName("Jones")
                .address("888 Oak Rd")
                .contactNo("4444444444")
                .gender(Gender.FEMALE)
                .email("lisa.jones@example.com")
                .noOfBooksLoan(4)
                .image("")
                .build();

        User savedUser9 = userRepository.save(user9);

        var auth9 = Authentication.builder()
                .id(9L)
                .email("lisa.jones@example.com")
                .password(passwordEncoder.encode("root"))
                .role(Role.USER)
                .user(savedUser9)
                .build();

        authenticationRepository.save(auth9);

        //
        var user10 = User.builder()
                .id(10L)
                .firstName("Peter")
                .lastName("Wang")
                .address("999 Pine Ave")
                .contactNo("1111111111")
                .gender(Gender.MALE)
                .email("peter.wang@example.com")
                .noOfBooksLoan(3)
                .image("")
                .build();

        User savedUser10 = userRepository.save(user10);

        var auth10 = Authentication.builder()
                .email("peter.wang@example.com")
                .password(passwordEncoder.encode("root"))
                .role(Role.USER)
                .user(savedUser10)
                .build();

        authenticationRepository.save(auth10);
    }

    @Test
//    @Disabled
    public void insertBookTest(){
        Book book1 = Book.builder()
                .bookCount(50)
                .authorName("J.R.R. Tolkien")
                .cost(new BigDecimal(25.99))
                .description("A fantasy novel about the journey of Bilbo Baggins.")
                .edition("First Edition")
                .imageURL("https://example.com/the-hobbit.jpg")
                .isbn("978-0-395-07122-9")
                .language("English")
                .pages(310)
                .publisherName("Houghton Mifflin")
                .title("The Hobbit")
                .build();
        bookRepository.save(book1);
        //
        Book book2 = Book.builder()
                .bookCount(30)
                .authorName("George Orwell")
                .cost(new BigDecimal(19.99))
                .description("A dystopian novel set in a totalitarian society.")
                .edition("Vintage Classics")
                .imageURL("https://example.com/1984.jpg")
                .isbn("978-0-452-28423-4")
                .language("English")
                .pages(328)
                .publisherName("Secker & Warburg")
                .title("1984")
                .build();
        bookRepository.save(book2);
        //
        Book book3 = Book.builder()
                .bookCount(40)
                .authorName("Dan Brown")
                .cost(new BigDecimal(22.50))
                .description("A thriller novel involving a hidden secret.")
                .edition("Special Illustrated Edition")
                .imageURL("https://example.com/da-vinci-code.jpg")
                .isbn("978-0-385-50420-1")
                .language("English")
                .pages(454)
                .publisherName("Doubleday")
                .title("The Da Vinci Code")
                .build();
        bookRepository.save(book3);
        //
        Book book4 = Book.builder()
                .bookCount(25)
                .authorName("Harper Lee")
                .cost(new BigDecimal(18.95))
                .description("A classic novel addressing racial injustice in the American South.")
                .edition("50th Anniversary Edition")
                .imageURL("https://example.com/to-kill-a-mockingbird.jpg")
                .isbn("978-0-06-112008-4")
                .language("English")
                .pages(336)
                .publisherName("J.B. Lippincott & Co.")
                .title("To Kill a Mockingbird")
                .build();
        bookRepository.save(book4);
        //
        Book book5 = Book.builder()
                .bookCount(35)
                .authorName("F. Scott Fitzgerald")
                .cost(new BigDecimal(20.50))
                .description("A novel set in the Jazz Age, exploring themes of decadence and excess.")
                .edition("Critical Edition")
                .imageURL("https://example.com/great-gatsby.jpg")
                .isbn("978-0-7432-7356-5")
                .language("English")
                .pages(180)
                .publisherName("Charles Scribner's Sons")
                .title("The Great Gatsby")
                .build();
        bookRepository.save(book5);
        //
        Book book6 = Book.builder()
                .bookCount(60)
                .authorName("J.K. Rowling")
                .cost(new BigDecimal(28.99))
                .description("The first book in the Harry Potter series.")
                .edition("Illustrated Edition")
                .imageURL("https://example.com/harry-potter.jpg")
                .isbn("978-0-590-35340-3")
                .language("English")
                .pages(320)
                .publisherName("Scholastic")
                .title("Harry Potter and the Sorcerer's Stone")
                .build();
        bookRepository.save(book6);
        //
        Book book7 = Book.builder()
                .bookCount(20)
                .authorName("J.D. Salinger")
                .cost(new BigDecimal(16.75))
                .description("A novel narrated by a disenchanted teenager, Holden Caulfield.")
                .edition("Reprint Edition")
                .imageURL("https://example.com/catcher-in-the-rye.jpg")
                .isbn("978-0-316-76948-0")
                .language("English")
                .pages(224)
                .publisherName("Little, Brown and Company")
                .title("The Catcher in the Rye")
                .build();
        bookRepository.save(book7);
        //
        Book book8 = Book.builder()
                .bookCount(28)
                .authorName("Jane Austen")
                .cost(new BigDecimal(21.25))
                .description("A classic novel exploring the themes of love and class in early 19th-century England.")
                .edition("Deluxe Edition")
                .imageURL("https://example.com/pride-and-prejudice.jpg")
                .isbn("978-0-14-143951-8")
                .language("English")
                .pages(416)
                .publisherName("Penguin Classics")
                .title("Pride and Prejudice")
                .build();
        bookRepository.save(book8);
        //
        Book book9 = Book.builder()
                .bookCount(45)
                .authorName("J.R.R. Tolkien")
                .cost(new BigDecimal(42.99))
                .description("An epic fantasy trilogy set in the world of Middle-earth.")
                .edition("50th Anniversary Edition")
                .imageURL("https://example.com/lord-of-the-rings.jpg")
                .isbn("978-0-345-45309-7")
                .language("English")
                .pages(1178)
                .publisherName("Ballantine Books")
                .title("The Lord of the Rings")
                .build();
        bookRepository.save(book9);
        //
        Book book10 = Book.builder()
                .bookCount(32)
                .authorName("Paulo Coelho")
                .cost(new BigDecimal(17.50))
                .description("A philosophical novel about a shepherd named Santiago and his journey.")
                .edition("25th Anniversary Edition")
                .imageURL("https://example.com/the-alchemist.jpg")
                .isbn("978-0-06-112241-5")
                .language("English")
                .pages(208)
                .publisherName("HarperOne")
                .title("The Alchemist")
                .build();
        bookRepository.save(book10);
        //
        Book book11 = Book.builder()
                .bookCount(55)
                .authorName("C.S. Lewis")
                .cost(new BigDecimal(50.75))
                .description("A series of seven high fantasy novels for children.")
                .edition("Box Set")
                .imageURL("https://example.com/narnia.jpg")
                .isbn("978-0-06-623850-0")
                .language("English")
                .pages(1728)
                .publisherName("HarperCollins")
                .title("The Chronicles of Narnia")
                .build();
        bookRepository.save(book11);
    }

    @Test
//    @Disabled
    public void insertCategoryTest(){
        Category category1 = Category.builder()
                .category("fantasy")
                .build();
        categoryRepository.save(category1);
        //
        Category category2 = Category.builder()
                .category("science fiction")
                .build();
        categoryRepository.save(category2);
        //
        Category category3 = Category.builder()
                .category("mystery")
                .build();
        categoryRepository.save(category3);
        //
        Category category4 = Category.builder()
                .category("romance")
                .build();
        categoryRepository.save(category4);
        //
        Category category5 = Category.builder()
                .category("historical fiction")
                .build();
        categoryRepository.save(category5);
        //
        Category category6 = Category.builder()
                .category("thriller")
                .build();
        categoryRepository.save(category6);
        //
        Category category7 = Category.builder()
                .category("horror")
                .build();
        categoryRepository.save(category7);
        //
        Category category8 = Category.builder()
                .category("non-fiction")
                .build();
        categoryRepository.save(category8);
        //
        Category category9 = Category.builder()
                .category("biography")
                .build();
        categoryRepository.save(category9);
        //
        Category category10 = Category.builder()
                .category("children's literature")
                .build();
        categoryRepository.save(category10);
    }
}
