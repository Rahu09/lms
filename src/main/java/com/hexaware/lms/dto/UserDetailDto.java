

package com.hexaware.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
