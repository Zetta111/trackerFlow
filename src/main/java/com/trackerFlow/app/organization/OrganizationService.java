package com.trackerFlow.app.organization;

import com.trackerFlow.app.dto.request.CreateOrganizationRequest;
import com.trackerFlow.app.dto.response.OrganizationResponseDto;
import com.trackerFlow.app.user.User;
import com.trackerFlow.app.user.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;




@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final OrganizationMemberRepository organizationMemberRepository;

    public OrganizationService(OrganizationRepository organizationRepository, UserRepository userRepository, OrganizationMemberRepository organizationMemberRepository) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.organizationMemberRepository = organizationMemberRepository;
    }


    private OrganizationResponseDto toOrganizationResponseDto(Organization organization){
        return new OrganizationResponseDto(
                organization.getId(),
                organization.getName(),
                organization.getDescription(),
                organization.getStatus()
        );
    }


    @Transactional
    public OrganizationResponseDto createOrganization(CreateOrganizationRequest request, Long userId) throws Exception {
        if(organizationRepository.existsByNameIgnoreCase(request.name())){
            throw new Exception("Name already taken");
        }
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No account found"));

        LocalDateTime now = LocalDateTime.now();
        Organization organization = new Organization();
        organization.setName(request.name());
        organization.setDescription(request.description());
        organization.setStatus(OrganizationStatus.ACTIVE);
        organization.setCreatedAt(now);
        organization.setUpdatedAt(now);

        Organization savedOrganization = organizationRepository.save(organization);

        OrganizationMember organizationMember= new OrganizationMember();
        organizationMember.setOrganization(savedOrganization);
        organizationMember.setUser(currentUser);
        organizationMember.setStatus(OrganizationMemberStatus.ACTIVE);
        organizationMember.setRole(OrganizationMemberRole.OWNER);
        organizationMember.setJoinedAt(now);
        organizationMember.setUpdatedAt(now);

        organizationMemberRepository.save(organizationMember);

        return toOrganizationResponseDto(savedOrganization);

    }



    @Transactional(readOnly = true)
    public OrganizationResponseDto getOrganizationById(Long organizationId,Long currentUserId) throws Exception {

        Organization currentOrganization = organizationRepository.findById(organizationId)
                .orElseThrow(()-> new RuntimeException("Organization not found"));

        if(currentOrganization.getStatus()== OrganizationStatus.ARCHIVED){
            throw new Exception("Organization is Archived");
        }

        if(!organizationMemberRepository.existsByOrganizationIdAndUserIdAndStatus(organizationId,currentUserId,OrganizationMemberStatus.ACTIVE)){
            throw new Exception("User doesnt exists in Organization");
        }

        return toOrganizationResponseDto(currentOrganization);
    }


}
