package com.trackerFlow.app.project;

import com.trackerFlow.app.organization.OrganizationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember,Long> {


    List<ProjectMember> findByProjectAndStatus(Project project,ProjectMemberStatus status);
    boolean existsByProjectAndOrganizationMember(Project project, OrganizationMember organizationMember);
    List<ProjectMember> findByOrganizationMemberAndStatus(OrganizationMember organizationMember,ProjectMemberStatus status);
}
