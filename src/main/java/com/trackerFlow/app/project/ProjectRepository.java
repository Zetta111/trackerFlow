package com.trackerFlow.app.project;

import com.trackerFlow.app.organization.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    boolean existsByOrganizationAndName(String name,Organization organization);
    List<Project> findByOrganizationAndStatus(Organization organization,ProjectStatus status);

    

}
