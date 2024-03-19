package com.hexaware.lms.service.impl;

import com.hexaware.lms.config.auth.JwtService;
import com.hexaware.lms.dto.*;
import com.hexaware.lms.entity.Authentication;
import com.hexaware.lms.entity.Notification;
import com.hexaware.lms.entity.Token;
import com.hexaware.lms.entity.User;
import com.hexaware.lms.exception.ResourceNotFoundException;
import com.hexaware.lms.repository.AuthenticationRepository;
import com.hexaware.lms.repository.NotificationRepository;
import com.hexaware.lms.repository.TokenRepository;
import com.hexaware.lms.repository.UserRepository;
import com.hexaware.lms.service.AuthenticationService;
import com.hexaware.lms.utils.TokenType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationRepository authenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final NotificationRepository notificationRepository;
    public AuthenticationResponse register(RegisterRequestDTO request) {
        log.debug("entered AuthenticationServiceImpl.register() service with args: {}",request.toString());

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .address(request.getAddress())
                .contactNo(request.getContactNo())
                .gender(request.getGender())
                .email(request.getEmail())
                .noOfBooksLoan(request.getNoOfBooksLoan())
                .image(request.getImage())
                .build();
        User savedUser = userRepository.save(user);


        var auth = Authentication.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .user(savedUser)
                .build();
        authenticationRepository.save(auth);

        var jwtToken = jwtService.generateToken(auth);

        revokeAllUserTokens(auth);
        saveUserToken(auth,jwtToken);

        AuthenticationResponse authenticationResponse = AuthenticationResponse
                .builder()
                .token(jwtToken)
                .email(auth.getEmail())
                .build();

        log.debug("exiting AuthenticationServiceImpl.register() service with return data: {}", authenticationResponse.toString());
        return authenticationResponse;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws ResourceNotFoundException {
        log.debug("entered AuthenticationServiceImpl.authenticate() service with args: {}",request.toString());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var auth = authenticationRepository.findByEmail(request.getEmail());
        if(auth.isEmpty()) throw new ResourceNotFoundException("authentication","email",request.getEmail());

        var user = userRepository.findById(auth.get().getId());
        var jwtToken = jwtService.generateToken(auth.get());


        revokeAllUserTokens(auth.get());
        saveUserToken(auth.get(),jwtToken);

        AuthenticationResponse authenticationResponse =  AuthenticationResponse
                .builder()
                .token(jwtToken)
                .email(auth.get().getEmail())
                .build();

        log.debug("exiting AuthenticationServiceImpl.authenticate() service with return data: {}", authenticationResponse.toString());
        return authenticationResponse;
    }

    @Override
    public UserDetailDto getDetails(String email) throws ResourceNotFoundException {
        log.debug("entered AuthenticationServiceImpl.UserDetailDto() service with args: {}",email);

        var auth = authenticationRepository.findByEmail(email);
        if(auth.isEmpty()) throw new ResourceNotFoundException("authentication","email",email);
        var user = userRepository.findById(auth.get().getId());

        if(user.isEmpty()) throw new ResourceNotFoundException("user" , "useremail", email);


        List<Notification> notificationList = notificationRepository.findByUser(user);
        List<NotificationResponseDTO> notificationDTOList =notificationList.stream().map(it-> {
            return NotificationResponseDTO.builder()
                    .userID(user.get().getId())
                    .id(it.getId())
                    .message(it.getMessage())
                    .seen(it.isSeen())
                    .type(it.getType())
                    .build();
        }).collect(Collectors.toList());

        UserDetailDto userDetailDto = UserDetailDto.builder()
                .noOfBooksLoan(user.get().getNoOfBooksLoan())
                .gender(user.get().getGender())
                .contactNo(user.get().getContactNo())
                .address(user.get().getAddress())
                .email(auth.get().getEmail())
                .firstName(user.get().getFirstName())
                .lastName(user.get().getLastName())
                .image(user.get().getImage())
                .role(auth.get().getRole().toString())
                .id(user.get().getId())
                .notification(notificationDTOList)
                .build();
        return userDetailDto;
    }

    private void revokeAllUserTokens(Authentication auth) {
        log.debug("entered AuthenticationServiceImpl.revokeAllUserTokens() service with args: {}",auth.toString());
        var validUserTokens = tokenRepository.findAllValidTokenByAuth(auth.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);

        log.debug("exiting AuthenticationServiceImpl.revokeAllUserTokens() service ");
    }

    private void saveUserToken(Authentication auth, String jwtToken) {
        log.debug("entered AuthenticationServiceImpl.revokeAllUserTokens() service with args: {} and {}",auth.toString(),jwtToken);
        var token = Token.builder()
                .user(auth)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);

        log.debug("exiting AuthenticationServiceImpl.revokeAllUserTokens() service ");
    }
}
