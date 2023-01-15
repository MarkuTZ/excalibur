package com.example.controllers;

import com.example.exception.GenericError;
import com.example.models.NewUserDTO;
import com.example.models.User;
import com.example.security.JwtUtils;
import com.example.services.UserService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LoginController {

	@Autowired
	private final UserService userService;

	@Autowired
	private final JwtUtils jwtUtils;

	@PostMapping("/token")
	public ResponseEntity<?> getToken(@RequestBody ObjectNode json) {
		String stringUsername = json.get("username").textValue();
		String stringPassword = json.get("password").textValue();
		Map<String, String> jsonResponse = new HashMap<>();
		User u = userService.getUser(stringUsername);
		if (u != null) {
			if (!userService.checkPassword(u, stringPassword)) {
				throw new GenericError(HttpStatus.BAD_REQUEST, "Wrong credentials!");
			}
			String token = jwtUtils.generateToken(stringUsername);
			jsonResponse.put("token", token);
			return ResponseEntity.ok(jsonResponse);
		}
		else {
			throw new GenericError(HttpStatus.NOT_FOUND, "User doesn't exist");
		}
	}

	@PostMapping("/register")
	public User registerUser(@RequestBody NewUserDTO user) {
		return userService.saveUserInDb(user);
	}

}
