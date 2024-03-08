package com.hexaware.lms.controller;

import com.hexaware.lms.dto.AuthenticationRequest;
import com.hexaware.lms.dto.AuthenticationResponse;
import com.hexaware.lms.dto.RegisterRequestDTO;
import com.hexaware.lms.dto.UserDetailDto;
import com.hexaware.lms.exception.ResourceNotFoundException;
import com.hexaware.lms.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequestDTO request
    ){
        log.debug("entered register() controller");
        log.info("Request received: {} - {}", "register()", "/api/v1/auth/register");
        AuthenticationResponse response = authenticationService.register(request);

        log.debug("exiting register() controller with HttpStatus.OK");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) throws ResourceNotFoundException {
        log.debug("entered authenticate() controller");
        log.info("Request received: {} - {}", "authenticate()", "/api/v1/auth/authenticate");

        AuthenticationResponse response = authenticationService.authenticate(request);

        log.debug("exiting authenticate() controller with HttpStatus.OK");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getDetails/{email}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<UserDetailDto> getDetails(
            @PathVariable("email") String email
    ) throws ResourceNotFoundException {
        log.debug("entered getDetails() controller");
        log.info("Request received: {} - {}", "getDetails()", "/api/v1/auth/getDetails");

        UserDetailDto userDetailDto = authenticationService.getDetails(email);
        log.debug("exiting getDetails() controller with HttpStatus.OK");
        return new ResponseEntity<>(userDetailDto,HttpStatus.OK);

    }
}
