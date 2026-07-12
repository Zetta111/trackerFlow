package com.trackerFlow.app.project;

import com.trackerFlow.app.dto.request.CreateProjectRequest;
import com.trackerFlow.app.dto.response.ProjectResponseDto;
import com.trackerFlow.app.organization.*;
import com.trackerFlow.app.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class ProjectService {

    private final OrganizationRepository organizationRepository ;
    private final UserRepository userRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public ProjectService(OrganizationRepository organizationRepository, UserRepository userRepository, OrganizationMemberRepository organizationMemberRepository,
                          ProjectRepository projectRepository,ProjectMemberRepository projectMemberRepository) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.organizationMemberRepository = organizationMemberRepository;
        this.projectRepository=projectRepository;
        this.projectMemberRepository=projectMemberRepository;
    }

    public ProjectResponseDto toProjectResponseDto(Project project){
        return new ProjectResponseDto(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStatus()
        );
    }


    @Transactional
    public ProjectResponseDto createProject(CreateProjectRequest request, Long organizationId, Long currentUserId) throws Exception {

        Organization currentOrganization=organizationRepository.findById(organizationId)
                .orElseThrow(()->new RuntimeException("organization not found"));
        if(currentOrganization.getStatus()== OrganizationStatus.ARCHIVED){
            throw new Exception("Organization is archived");
        }

        OrganizationMember organizationMember=organizationMemberRepository.findByOrganizationIdAndUserIdAndStatus(organizationId,
                currentUserId, OrganizationMemberStatus.ACTIVE)
                .orElseThrow(()->new RuntimeException("Organization Member not found"));

        if(!Objects.equals(currentOrganization.getOwner().getId(), currentUserId)&&(
                organizationMember.getRole()!=OrganizationMemberRole.ADMIN)){
            throw new Exception("You are not allowed to create a project");
        }

        String currentName=request.name().trim();
        if(currentName.isBlank()){
            throw new Exception("Name must be filled out");
        }
        if(projectRepository.existsByOrganizationAndNameIgnoreCase(currentOrganization,currentName)){
            throw new Exception("Name already exists");
        }
        LocalDateTime now = LocalDateTime.now();
        Project project= new Project();
        project.setName(currentName);
        project.setOrganization(currentOrganization);
        project.setDescription(request.description());
        project.setCreatedAt(now);
        project.setUpdatedAt(now);
        project.setStatus(ProjectStatus.ACTIVE);
        Project savedProject=projectRepository.save(project);

        ProjectMember projectMember=new ProjectMember();
        projectMember.setProject(savedProject);
        projectMember.setOrganizationMember(organizationMember);
        projectMember.setStatus(ProjectMemberStatus.ACTIVE);
        projectMember.setRole(ProjectMemberRole.PROJECT_MANAGER);
        projectMember.setCreatedAt(now);
        projectMember.setUpdatedAt(now);
        projectMemberRepository.save(projectMember);

        return toProjectResponseDto(savedProject);

    }


    public ProjectResponseDto getProject(Long organizationId,Long projectId,Long currentUserId){
        Organization currentOrganization=organizationRepository.findById(organizationId)
                .orElseThrow(()->new RuntimeException("Organization not found"));

        Project currentProject=projectRepository.findById(projectId)
                .orElseThrow(()-> new RuntimeException("Project not found"));

        OrganizationMember currentOrganizationMember=organizationMemberRepository.findByOrganizationIdAndUserIdAndStatus(organizationId,currentUserId,OrganizationMemberStatus.ACTIVE)
                .orElseThrow(()-> new RuntimeException("Not organization member found"));

        if(!projectMemberRepository.existsByProjectAndOrganizationMember(currentProject,currentOrganizationMember)){
            throw new RuntimeException("You are not part of this project");
        }

        return toProjectResponseDto(currentProject);
    }
}
