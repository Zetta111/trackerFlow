package com.trackerFlow.app.organization;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization,Long> {

    boolean existsByName(String name);
    Organization findByName(String name);
    void Save(Organization organization);
}
