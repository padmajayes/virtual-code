package com.example.demo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
public class AuthenticationController {

	private AuthenticationService authenticationService;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@PostMapping(value = "addUser/username/{username}/password/{password}", produces = { "application/json" })

	public void addConverter

	(@PathVariable String username, @PathVariable String password) {

		Account account = new Account();
		account.setFirstname(username);
		account.setLastname(username);
		account.setUsername(username);
		account.setPassword(passwordEncoder.encode(password));

		accountRepository.save(account);

	}

	@PostMapping("/login")
	public ResponseEntity<JWTTokenResponse> createCustomer(@RequestBody AuthenticationRequest request) {
		return new ResponseEntity<>(
				authenticationService.generateJWTToken(request.getUsername(), request.getPassword()), HttpStatus.OK);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
}
