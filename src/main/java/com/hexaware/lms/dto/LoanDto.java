package com.hexaware.lms.dto;

import com.hexaware.lms.entity.Book;
import com.hexaware.lms.entity.User;
import com.hexaware.lms.utils.LoanStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDto {

    private OffsetDateTime issueDate;

    private OffsetDateTime returnDate;

    @Enumerated(value = EnumType.STRING)
    private LoanStatus status;

    private User user;

    private Book book;
}