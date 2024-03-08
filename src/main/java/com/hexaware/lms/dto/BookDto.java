package com.hexaware.lms.dto;


import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDto {
    private Long id;
    @NotBlank
    private String title;
    @Size(min = 5)
    private String isbn;
    @NotBlank
    private String authorName;
    private String publisherName;
    private String edition;
    private String description;
    @NotBlank
    private String language;//
    @PositiveOrZero
    private int pages;
    @Positive
    private BigDecimal cost;//
    @Min(1)
    private int bookCount;
    @NotEmpty
    private String imageURL;

}