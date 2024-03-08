package com.hexaware.lms;

import com.hexaware.lms.entity.Authentication;
import com.hexaware.lms.repository.AuthenticationRepository;
import com.hexaware.lms.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.logging.Logger;

@Slf4j
@SpringBootApplication
@RestController
public class LmsApplication {
	public static void main(String[] args) {
		log.debug("Applicaiton is running");
		SpringApplication.run(LmsApplication.class, args);

	}


}
