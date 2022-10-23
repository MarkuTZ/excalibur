package com.example.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name="user")
@Data

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ID;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String token;
}
