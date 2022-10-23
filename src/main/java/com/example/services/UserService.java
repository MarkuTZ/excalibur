package com.example.services;

import com.example.models.User;
import com.example.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
    @Autowired
    UserRepository userRepository;

    @Value("${app.secret.key}")
    private String secret_key;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // code to generate Token
    public String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)))
                .signWith(signatureAlgorithm, Base64.getEncoder().encode(secret_key.getBytes()))
                .compact();
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

    public boolean isValidToken(String token, String username) {
        return !isTokenExpired(token) && validUsername(token, username);
    }

    // code to get body
    public Claims getBody(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encode(secret_key.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    //code to get subject
    public String getSubject(String token) {
        return getBody(token).getSubject();
    }

    // code to check if token is valid as per username
    public boolean validUsername(String token, String username) {
        String tokenUserName = getSubject(token);
        return (username.equals(tokenUserName));
    }

    // code to check if token is expired
    public boolean isTokenExpired(String token) {
        return getBody(token).getExpiration().before(new Date(System.currentTimeMillis()));
    }


}
