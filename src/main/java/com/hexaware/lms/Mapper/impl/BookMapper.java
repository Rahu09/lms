package com.hexaware.lms.Mapper.impl;

import com.hexaware.lms.Mapper.Mapper;
import com.hexaware.lms.dto.BookDto;
import com.hexaware.lms.entity.Book;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
    public class BookMapper implements Mapper<Book, BookDto> {

    private final ModelMapper modelMapper;

    @Override
    public BookDto mapTo(Book book) {
        return modelMapper.map(book, BookDto.class);
    }

    @Override
    public Book mapFrom(BookDto bookDto) {
        return modelMapper.map(bookDto, Book.class);
    }
}
