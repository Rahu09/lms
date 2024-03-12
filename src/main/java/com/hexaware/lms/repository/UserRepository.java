package com.hexaware.lms.repository;

import com.hexaware.lms.dto.ReservationDto;
import com.hexaware.lms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u.noOfBooksLoan FROM User u WHERE u.email = :userEmail")
    Integer findNoOfBooksLoanByEmail(String userEmail);

    @Query("SELECT new com.hexaware.lms.dto.ReservationDto(r.id, r.user, r.book, r.issueTimestamp, b.title) FROM Reservation r JOIN r.book b WHERE r.user.id = :userId")
    List<ReservationDto> findReservationHistory(Long userId);

}
