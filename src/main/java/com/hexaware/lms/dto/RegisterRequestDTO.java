package com.hexaware.lms.dto;

import com.hexaware.lms.utils.Gender;
import com.hexaware.lms.utils.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
//    @Pattern.List({
//            @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter"),
//            @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter"),
//            @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one digit"),
//            @Pattern(regexp = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?].*", message = "Password must contain at least one special character")
//    })
    private String password;
    @NotBlank(message = "role cannot be blank")
    private Role role;
    @Size(max = 30, min = 3, message = "Invalid firstName. Size should be between 3 to 30.")
    private String firstName;
    @Size(max = 30, min = 3, message = "Invalid lastName. Size should be between 3 to 30.")
    private String lastName;
    @Size(max = 14, min = 9, message = "Invalid contactNo. Size should be between 9 to 14.")
    private String contactNo;
    @Size(max = 50, min = 10, message = "Invalid lastName. Size should be between 10 to 50.")
    private String address;
    @NotNull
    private int noOfBooksLoan;
    @NotNull
    private Gender gender;
    private String image;
}
