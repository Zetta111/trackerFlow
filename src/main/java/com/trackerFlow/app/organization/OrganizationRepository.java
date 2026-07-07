package com.trackerFlow.app.organization;


import com.trackerFlow.app.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization,Long> {

    boolean existsByOwner_IdAndNameIgnoreCase(Long ownerId, String name);
    Organization findByName(String name);
    boolean existsByOwner_IdAndNameIgnoreCaseAndIdNot(Long ownerId,String name,Long organizationId);

}
