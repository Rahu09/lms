package com.hexaware.lms.service.impl;

import com.hexaware.lms.config.auth.JwtService;
import com.hexaware.lms.dto.AuthenticationRequest;
import com.hexaware.lms.dto.AuthenticationResponse;
import com.hexaware.lms.dto.RegisterRequestDTO;
import com.hexaware.lms.entity.Authentication;
import com.hexaware.lms.entity.Token;
import com.hexaware.lms.entity.User;
import com.hexaware.lms.exception.ResourceNotFoundException;
import com.hexaware.lms.repository.AuthenticationRepository;
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
        var jwtToken = jwtService.generateToken(auth.get());

        revokeAllUserTokens(auth.get());
        saveUserToken(auth.get(),jwtToken);
        AuthenticationResponse authenticationResponse =  AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();

        log.debug("exiting AuthenticationServiceImpl.authenticate() service with return data: {}", authenticationResponse.toString());
        return authenticationResponse;
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
