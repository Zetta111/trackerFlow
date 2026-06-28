package com.trackerFlow.app.project;

import com.trackerFlow.app.organization.OrganizationMember;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="project_members")
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="project_member_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="project_id",nullable=false)
    private Project project;

    @ManyToOne
    @JoinColumn(name="organization_member_id",nullable=false)
    private OrganizationMember organizationMember;

    @Enumerated(EnumType.STRING)
    @Column(name="role",nullable=false)
    private ProjectMemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(name="status",nullable=false)
    private ProjectMemberStatus status;

    @Column(name="created_at",nullable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at",nullable=false)
    private LocalDateTime updatedAt;
}
