package com.trackerFlow.app.organization;

import com.trackerFlow.app.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember,Long> {

    boolean ExistsByOrganizationAndUser(Organization organization,User user);
    List<OrganizationMember>FindByOrganizationAndStatus(Organization organization,OrganizationMemberStatus status);
    List<OrganizationMember>FindByUserAndStatus(User user,OrganizationMemberStatus status);
}
