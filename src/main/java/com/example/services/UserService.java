package com.example.services;

import com.example.models.User;
import com.example.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
        return userRepository.findByUsername(username);
    }

    //check user
    public User existUser(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean checkPassword(User u, String pass) {
        return passwordEncoder.matches(pass, u.getPassword());
    }

    //save user in DB
    public User saveUserInDb(String username, String password) {
        User u = new User();
        if (existUser(username) == null) {
            u.setUsername(username);
            u.setPassword(passwordEncoder.encode(password));
            return userRepository.save(u);
        } else {
            return null;
        }
    }
}
