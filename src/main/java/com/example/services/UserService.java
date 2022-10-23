package com.example.services;

import com.example.models.User;
import com.example.repositories.UserRepository;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.SignatureAlgorithm;
import java.sql.Date;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import io.jsonwebtoken.Jwts;
@Service
public class UserService {

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
    @Autowired
    UserRepository userRepository;

    @Value("${app.secret.key}")
    private String secret_key;

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
    public boolean existUser(String username, String password)
    {
        User u= new User();
        u= userRepository.findByUsername(username);
        if((u!=null) && (u.getPassword().equals(password))) return true;
        else return false;
    }
    //save user in DB
    public void saveUserinDb(String username, String password)
    {
        User u = new User();
        u.setUsername(username);
        u.setPassword(password);
        userRepository.save(u);
    }
    public boolean isValidToken(String token, String username)
    {
        return !isTokenExpired(token) && validUsername(token,username);
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
    public boolean validUsername(String token,String username)
    {
        String tokenUserName=getSubject(token);
        return (username.equals(tokenUserName));
    }

    // code to check if token is expired
    public boolean isTokenExpired(String token)
    {
        return  getBody(token).getExpiration().before(new Date(System.currentTimeMillis()));
    }









}
