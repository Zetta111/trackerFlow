package com.trackerFlow.app.organization;

import com.trackerFlow.app.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember,Long> {

    boolean existsByOrganizationAndUser(Organization organization,User user);
    boolean existsByOrganizationIdAndUserIdAndStatus(Long organizationId,Long userId,OrganizationMemberStatus status);
    List<OrganizationMember>findByOrganizationAndStatus(Organization organization,OrganizationMemberStatus status);
    List<OrganizationMember>findByUserAndStatus(User user,OrganizationMemberStatus status);
    boolean existsByOrganizationIdAndUserIdAndRoleAndStatus(Long organizationId, Long userId, OrganizationMemberRole role, OrganizationMemberStatus status);
}
