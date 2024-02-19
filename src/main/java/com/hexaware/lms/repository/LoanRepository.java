package com.hexaware.lms.repository;


import com.hexaware.lms.entity.Book;
import com.hexaware.lms.entity.Loan;
import com.hexaware.lms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findAllByBook(Book book);

    List<Loan> findAllByUser(User user);

    @Query("SELECT l FROM Loan l WHERE DATEDIFF(l.returnDate, l.issueDate) > 7")
    List<Loan> overdueLoans();

    Loan findByBook(Optional<Book> byId);
}
