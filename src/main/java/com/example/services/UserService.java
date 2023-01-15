package com.example.services;

import com.example.exception.GenericError;
import com.example.models.NewUserDTO;
import com.example.models.User;
import com.example.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

	@Autowired
	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public User getUser(String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new GenericError(HttpStatus.NOT_FOUND, "User " + username + " doesn't exist");
		}
		return user;
	}

	public boolean checkPassword(User u, String pass) {
		return passwordEncoder.matches(pass, u.getPassword());
	}

	// save user in DB
	public User saveUserInDb(NewUserDTO newUser) {
		if (userRepository.findByUsername(newUser.getUsername()) != null) {
			throw new GenericError(HttpStatus.BAD_REQUEST, "Username " + newUser.getUsername() + " already exists!");
		}
		User user = new User();
		user.setUsername(newUser.getUsername());
		user.setPassword(passwordEncoder.encode(newUser.getPassword()));
		return userRepository.save(user);
	}

}
