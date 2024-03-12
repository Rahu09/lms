package com.hexaware.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookFilterDto {
    List<String> authorList;
    List<String> categoryList;
    List<String> languageList;
}
