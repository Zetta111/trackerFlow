package com.trackerFlow.app.user;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id",nullable=false,unique = true)
    private Long id;

    @Column(name="first_name",nullable=false,unique = false)
    private String firstName;

    @Column(name="last_name",nullable=false,unique = false)
    private String lastName;

    @Column(name="created_at",nullable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at",nullable=false)
    private LocalDateTime updatedAt;

    @Column(name="password_hash",nullable=false)
    private String passwordHash;

    @Column(name="email",nullable=false,unique = true)
    private String email;
}
