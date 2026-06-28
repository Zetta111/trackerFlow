package com.trackerFlow.app.organization;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="organization_member")
public class OrganizationMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="organization_member",unique=true,nullable=false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="organization_id",nullable=false)
    private Organization organization;


}
