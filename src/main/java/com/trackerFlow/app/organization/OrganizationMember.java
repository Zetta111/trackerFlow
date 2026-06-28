package com.trackerFlow.app.organization;

import com.trackerFlow.app.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="organization_member")
public class OrganizationMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="organization_member_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="organization_id",nullable=false)
    private Organization organization;

    @ManyToOne
    @JoinColumn(name="user_id",nullable=false)
    private User user;

    @Column(name="joined_at",nullable=false)
    private LocalDateTime joinedAt;

    @Column(name="updated_at",nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name="role",nullable=false)
    private OrganizationMemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(name="status",nullable=false)
    private OrganizationMemberStatus status;
}
