package com.hexaware.lms.entity;

import com.hexaware.lms.utils.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    private Long id;
    @Column(unique = true)
    private String email;
    private String firstName;
    private String lastName;
    private String contactNo;
    private String address;
    private int noOfBooksLoan;
    private String image="";

    @Enumerated(value = EnumType.STRING)
    private Gender gender;
}

