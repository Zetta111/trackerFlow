package com.trackerFlow.app.project;

import com.trackerFlow.app.organization.Organization;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="project_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="organization_id",nullable=false)
    private Organization organization;

    @Column(name="name",nullable=false,unique = true)
    private String name;

    @Column(name="created_at",nullable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at",nullable=false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name="status",nullable=false)
    private ProjectStatus status;

    @Column(name="description",nullable=true)
    private String description;
}
