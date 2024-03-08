package com.hexaware.lms.service;


import com.hexaware.lms.dto.AuthenticationRequest;
import com.hexaware.lms.dto.AuthenticationResponse;
import com.hexaware.lms.dto.RegisterRequestDTO;
import com.hexaware.lms.dto.UserDetailDto;
import com.hexaware.lms.exception.ResourceNotFoundException;

public interface AuthenticationService {
    public AuthenticationResponse register(RegisterRequestDTO request);
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws ResourceNotFoundException;

    UserDetailDto getDetails(String email) throws ResourceNotFoundException;
}
