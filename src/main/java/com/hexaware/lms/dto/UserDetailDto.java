package com.hexaware.lms.dto;

import com.hexaware.lms.utils.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDto {
    private String role;
    private String firstName;
    private String lastName;
    private String email;
    private String image;
    private Long id;

    private String contactNo;
    private String address;
    private int noOfBooksLoan;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    private List<NotificationResponseDTO> notification;
}