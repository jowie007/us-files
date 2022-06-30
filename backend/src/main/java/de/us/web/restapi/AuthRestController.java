package de.us.web.restapi;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.us.web.domain.RoleEnum;
import de.us.web.dto.UserDto;
import de.us.web.payload.request.LoginRequest;
import de.us.web.payload.response.JwtResponse;
import de.us.web.payload.response.AnswerResponse;
import de.us.web.repositories.UserRepository;
import de.us.web.security.jwt.JwtUtils;
import de.us.web.security.services.UserDetailsImpl;
import de.us.web.services.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

	private AuthenticationManager authenticationManager;

	private UserRepository userRepository;

	private PasswordEncoder encoder;

	private JwtUtils jwtUtils;

	private UserService userService;

	@Autowired
	public AuthRestController(AuthenticationManager authenticationManager, UserRepository userRepository,
			PasswordEncoder encoder, JwtUtils jwtUtils, UserService userService) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.encoder = encoder;
		this.jwtUtils = jwtUtils;
		this.userService = userService;
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getEmail(), roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto) {
		if (userRepository.existsByEmail(userDto.getEmail())) {
			return ResponseEntity.badRequest().body(new AnswerResponse("Error: Email is already in use!"));
		}

		if (userRepository.existsByName(userDto.getName())) {
			return ResponseEntity.badRequest().body(new AnswerResponse("Error: Username is already taken!"));
		}

		// Create new user's account
		userDto.setPassword(encoder.encode(userDto.getPassword()));
		userDto.setRole(RoleEnum.ROLE_USER);
		userDto.setRegistrationDate(new Timestamp(System.currentTimeMillis()));
		userService.createUser(userDto);
		
		return ResponseEntity.ok(new AnswerResponse("User registered successfully!"));
	}
}