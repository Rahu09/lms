package com.hexaware.lms.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookReservationHistoryDTO {
    private Long id;

    private String user;

    private OffsetDateTime issueTimestamp;
}
