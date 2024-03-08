package com.hexaware.lms.repository;

import com.hexaware.lms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u.noOfBooksLoan FROM User u WHERE u.email = :userEmail")
    Integer findNoOfBooksLoanByEmail(String userEmail);

}
