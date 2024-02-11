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
    private String email;
    private String firstName;
    private String lastName;
    private int contactNo;
    private String address;
    private int noOfBooksLoan;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    //foreign key connection
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Authentication authentication;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> notification;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Loan> loan;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Reservation> reservation;
}
