package com.example.repositories;

import com.example.models.User;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
@Component

public interface UserRepository extends JpaRepository<User,Long>
{
    @Query(value = "Select u FROM User u where u.username = ?1 and u.password = ?2")
    User findUser(String username, String password);
}
