package burundi.ilucky.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import burundi.ilucky.payload.AuthRequest;
import burundi.ilucky.payload.AuthResponse;
import burundi.ilucky.payload.Response;
import burundi.ilucky.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> auth(@Valid @RequestBody AuthRequest authRequest) {
		return new ResponseEntity<>(authService.login(authRequest), HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<Response> register(@Valid @RequestBody AuthRequest authRequest) {

		return new ResponseEntity<>(authService.register(authRequest), HttpStatus.OK);
	}

	@PostMapping("/logout")
	public ResponseEntity<Response> logout(@Valid HttpServletRequest request) {

		return new ResponseEntity<>(authService.logout(request), HttpStatus.OK);
	}
}
