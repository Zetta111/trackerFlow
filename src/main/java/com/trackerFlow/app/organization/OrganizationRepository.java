package com.trackerFlow.app.organization;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization,Long> {

    boolean existsByNameIgnoreCase(String name);
    Organization findByName(String name);

}
