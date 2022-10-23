package com.example.services;

import com.example.models.User;
import com.example.repositories.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Service
public class UserService  {

    @Autowired
    private UserRepository userRepository;

    public String generateToken(String username, String password)
    {
         User user = userRepository.findUser(username,password);
         StringBuilder token = new StringBuilder();
         String tokenFinal;
         if(user != null)
         {
             Timestamp timestamp = new Timestamp(System.currentTimeMillis());
             long currentTimeInMilisecond = timestamp.getTime();
             tokenFinal=token.append(currentTimeInMilisecond).append("-")
                     .append(UUID.randomUUID().toString()).toString();
             user.setToken(tokenFinal);
             userRepository.save(user);
             return tokenFinal;
         }
         else return null;
    }
}
