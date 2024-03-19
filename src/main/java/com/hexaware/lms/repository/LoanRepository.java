package com.hexaware.lms.repository;


import com.hexaware.lms.entity.Book;
import com.hexaware.lms.entity.Loan;
import com.hexaware.lms.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findAllByBook(Book book);

    List<Loan> findAllByUser(User user);

    @Query("SELECT l FROM Loan l WHERE DATEDIFF(l.returnDate, l.issueDate) > 7")
    List<Loan> overdueLoans();

    Loan findByBook(Optional<Book> byId);

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.book.id = :id AND l.status = 'LOAN'")
    Integer getLoanCount(Long id);

    @Query("SELECT COUNT(l) FROM Loan l WHERE DATE(l.issueDate) = DATE(:issueDate) AND l.status = 'LOAN'")
    Integer getLoanCountForDate(OffsetDateTime issueDate);

    @Query("SELECT l FROM Loan l WHERE l.status = 'LATE'")
    List<Loan> findAllLateLoans();

    Loan findByUserIdAndBookId(Long userId, Long bookId);

    @Query("SELECT l FROM Loan l WHERE l.status = 'LOAN' AND l.issueDate <= :issueDate")
    List<Loan> getAllLoansIssueDate(OffsetDateTime issueDate);

    @Transactional
    @Modifying
    @Query("UPDATE Loan l SET l.status = 'LATE' WHERE l.issueDate <= :issueDate AND l.status = 'LOAN'")
    void updateStatusToLateByIssueDate(OffsetDateTime issueDate);
}
