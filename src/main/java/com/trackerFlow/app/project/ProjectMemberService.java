package com.trackerFlow.app.project;

import com.trackerFlow.app.dto.response.ProjectResponseDto;
import com.trackerFlow.app.organization.OrganizationMemberRepository;
import com.trackerFlow.app.organization.OrganizationRepository;
import com.trackerFlow.app.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectMemberService {


    private final OrganizationRepository organizationRepository ;
    private final UserRepository userRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public ProjectMemberService(OrganizationRepository organizationRepository, UserRepository userRepository, OrganizationMemberRepository organizationMemberRepository,
                          ProjectRepository projectRepository,ProjectMemberRepository projectMemberRepository) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.organizationMemberRepository = organizationMemberRepository;
        this.projectRepository=projectRepository;
        this.projectMemberRepository=projectMemberRepository;
    }






}
