package com.hexaware.lms.Mapper.impl;


import com.hexaware.lms.Mapper.Mapper;
import com.hexaware.lms.dto.UserDetailDto;
import com.hexaware.lms.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper implements Mapper<User, UserDetailDto> {
    private final ModelMapper modelMapper;

    @Override
    public UserDetailDto mapTo(User user) {
        return modelMapper.map(user, UserDetailDto.class);
    }

    @Override
    public User mapFrom(UserDetailDto userDetailDto) {
        return modelMapper.map(userDetailDto, User.class);
    }
}