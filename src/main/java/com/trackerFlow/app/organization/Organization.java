package com.trackerFlow.app.organization;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name="organization")

public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="organization_id")
    private Long id;

    @Column(name="name",nullable=false,unique = true)
    private String name;

    @Column(name="description",nullable = true)
    private String description;

    @Column(name="created_at",nullable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at",nullable=false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name="status",nullable=false)
    private OrganizationStatus status;


}
