package com.hexaware.lms.dto;

import com.hexaware.lms.entity.Book;
import com.hexaware.lms.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDto {

    private Long id;

    private Long bookId;

    private String imgUrl;

    private OffsetDateTime issueTimestamp;

    private String bookName;
}