package com.example.controllers;

import com.example.models.User;
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
public class RegisterController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody ObjectNode json) {
        String stringUsername = json.get("username").textValue();
        String stringPassword = json.get("password").textValue();
        Map<String, String> jsonResponse = new HashMap<>();
        if (!userService.existUser(stringUsername)) {
            User u= userService.saveUserinDb(stringUsername, stringPassword);
            jsonResponse.put(".", "Successfully registered user");
            return ResponseEntity.ok(u);
        } else {
            jsonResponse.put("Error:", "User already exist");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
        }

    }
}
