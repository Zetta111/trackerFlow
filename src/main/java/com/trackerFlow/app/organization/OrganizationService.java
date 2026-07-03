package com.trackerFlow.app.organization;

import com.trackerFlow.app.user.User;
import com.trackerFlow.app.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.trackerFlow.app.organization.OrganizationStatus.ACTIVE;

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
    @Transactional
    OrganizationResponseDto createOrganization(CreateOrganizationRequest request,Long userId) throws Exception {
        if(organizationRepository.existsByName(request.getName())){
            throw new Exception("Name already taken");
        }
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No account found"));


        Organization organization = new Organization();
        organization.setName(request.getName());
        organization.setDescription(request.getDescription());
        organization.setStatus(OrganizationStatus.ACTIVE);
        organization.setCreatedAt(LocalDateTime.now());
        organization.setUpdatedAt(LocalDateTime.now());

        Organization savedOrganization = organizationRepository.save(organization);

        OrganizationMember organizationMember= new OrganizationMember();
        organizationMember.setOrganization(savedOrganization);
        organizationMember.setUser(currentUser);
        organizationMember.setStatus(OrganizationMemberStatus.ACTIVE);
        organizationMember.setRole(OrganizationMemberRole.OWNER);
        organizationMember.setJoinedAt(LocalDateTime.now());
        organizationMember.setUpdatedAt(LocalDateTime.now());

        OrganizationMember savedOrganizationMember=organizationMemberRepository.save(organizationMember);

        return OrganizationResponseDto;

    }


}
