package com.trackerFlow.app.project;

import com.trackerFlow.app.dto.request.CreateProjectRequest;
import com.trackerFlow.app.dto.request.UpdateProjectRequest;
import com.trackerFlow.app.dto.response.ProjectResponseDto;
import com.trackerFlow.app.dto.response.UpdatedResponseDto;
import com.trackerFlow.app.organization.*;
import com.trackerFlow.app.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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

    @Transactional(readOnly = true)
    public ProjectResponseDto getProject(Long organizationId,Long projectId,Long currentUserId){
        Organization currentOrganization=organizationRepository.findById(organizationId)
                .orElseThrow(()->new RuntimeException("Organization not found"));

        if(currentOrganization.getStatus()==OrganizationStatus.ARCHIVED){
            throw new RuntimeException("Organization is archived");
        }

        Project currentProject=projectRepository.findById(projectId)
                .orElseThrow(()-> new RuntimeException("Project not found"));

        if(currentProject.getStatus()==ProjectStatus.ARCHIVED){
            throw new RuntimeException("Project archived");
        }
        OrganizationMember currentOrganizationMember=organizationMemberRepository.findByOrganizationIdAndUserIdAndStatus(organizationId,currentUserId,OrganizationMemberStatus.ACTIVE)
                .orElseThrow(()-> new RuntimeException("Not organization member found"));

        if(!projectMemberRepository.existsByProjectAndOrganizationMember(currentProject,currentOrganizationMember)){
            throw new RuntimeException("You are not part of this project");
        }

        return toProjectResponseDto(currentProject);
    }

    @Transactional
    public UpdatedResponseDto updateProject(UpdateProjectRequest request,Long currentUserId, Long projectId, Long organizationId) throws Exception {
        Organization currentOrganization=organizationRepository.findById(organizationId)
                .orElseThrow(()-> new RuntimeException("organization not found"));

        if(currentOrganization.getStatus()==OrganizationStatus.ARCHIVED){
            throw new RuntimeException("Organization is archived");
        }

        Project currentProject=projectRepository.findById(projectId)
                .orElseThrow(()->new RuntimeException("project not found"));

        if(currentProject.getStatus()==ProjectStatus.ARCHIVED){
            throw new RuntimeException("Project archived");
        }

        OrganizationMember organizationMember=organizationMemberRepository.findByOrganizationIdAndUserIdAndStatus(organizationId,currentUserId,OrganizationMemberStatus.ACTIVE)
                .orElseThrow(()-> new RuntimeException("organization member not found"));

        if(!Objects.equals(currentOrganization.getOwner().getId(), currentUserId)&&(
                organizationMember.getRole()!=OrganizationMemberRole.ADMIN)){
            throw new Exception("You are not allowed to update a project");
        }

        String name=request.name().trim();
        if(projectRepository.existsByOrganizationAndNameIgnoreCase(currentOrganization,name)){
            throw new Exception("Name already exists");
        }
        if(name.isBlank()){
            throw new Exception("Name cannot be empty");
        }

        currentProject.setName(name);
        currentProject.setDescription(request.description());
        currentProject.setUpdatedAt(LocalDateTime.now());
        projectRepository.save(currentProject);

        return new UpdatedResponseDto(
                currentProject.getName(), currentProject.getDescription()
        );

    }


    @Transactional
    public void archiveProject(Long currentUserId,Long organizationId,Long projectId) throws Exception {
        Organization currentOrganization=organizationRepository.findById(organizationId)
                .orElseThrow(()-> new RuntimeException("organization not found"));

        if(currentOrganization.getStatus()==OrganizationStatus.ARCHIVED){
            throw new RuntimeException("Organization is archived");
        }

        Project currentProject=projectRepository.findById(projectId)
                .orElseThrow(()->new RuntimeException("project not found"));

        if(currentProject.getStatus()==ProjectStatus.ARCHIVED){
            throw new RuntimeException("Project archived already");
        }

        OrganizationMember organizationMember=organizationMemberRepository.findByOrganizationIdAndUserIdAndStatus(organizationId,currentUserId,OrganizationMemberStatus.ACTIVE)
                .orElseThrow(()-> new RuntimeException("organization member not found"));

        if(!Objects.equals(currentOrganization.getOwner().getId(), currentUserId)&&(
                organizationMember.getRole()!=OrganizationMemberRole.ADMIN)){
            throw new Exception("You are not allowed to update a project");
        }

        currentProject.setStatus(ProjectStatus.ARCHIVED);
        currentProject.setUpdatedAt(LocalDateTime.now());
        projectRepository.save(currentProject);
    }

    public List<ProjectResponseDto> listActiveProjects(Long currentUserId,Long organizationId) throws Exception {
        OrganizationMember currentOrganizationMember=organizationMemberRepository.findByOrganizationIdAndUserIdAndStatus(organizationId,currentUserId,OrganizationMemberStatus.ACTIVE)
                .orElseThrow(()->new RuntimeException("organization member not found"));

        Organization currentOrganization= organizationRepository.findById(organizationId)
                .orElseThrow(()->new RuntimeException("organization not found"));

        List<Project> projects =projectRepository.findByOrganizationAndStatus(currentOrganization,ProjectStatus.ACTIVE);

        if(Objects.equals(currentOrganization.getOwner().getId(), currentUserId)||(
                currentOrganizationMember.getRole()!=OrganizationMemberRole.ADMIN)){
            return projects.stream()
                    .map(this::toProjectResponseDto)
                    .toList();
        }else{
            List<ProjectMember> projectMembers=projectMemberRepository.findByOrganizationMemberAndStatus(currentOrganizationMember,ProjectMemberStatus.ACTIVE);
            return projectMembers.stream()
                    .map(ProjectMember::getProject).map(this::toProjectResponseDto).toList();
        }







    }



}
