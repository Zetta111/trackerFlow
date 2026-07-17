package com.trackerFlow.app.organization;

import com.trackerFlow.app.dto.request.AddOrganizationMemberRequest;
import com.trackerFlow.app.dto.response.OrganizationMemberResponseDto;
import com.trackerFlow.app.dto.response.OrganizationResponseDto;
import com.trackerFlow.app.user.User;
import com.trackerFlow.app.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class OrganizationMemberService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final OrganizationMemberRepository organizationMemberRepository;

    public OrganizationMemberService(OrganizationRepository organizationRepository, UserRepository userRepository, OrganizationMemberRepository organizationMemberRepository) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.organizationMemberRepository = organizationMemberRepository;
    }

    public OrganizationMemberResponseDto toOrganizationMemberResponseDto(OrganizationMember organizationMember) {
        return new OrganizationMemberResponseDto(
                organizationMember.getId(),
                organizationMember.getUser().getId(),
                organizationMember.getUser().getFirstName(),
                organizationMember.getUser().getLastName(),
                organizationMember.getStatus(),
                organizationMember.getRole()
        );
    }

    @Transactional
    public OrganizationMemberResponseDto addOrganizationMember(AddOrganizationMemberRequest request, Long organizationId, Long currentUserId) throws Exception {

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new Exception("Organization not found"));

        if (!Objects.equals(organization.getOwner().getId(), currentUserId) &&
                !organizationMemberRepository.existsByOrganizationIdAndUserIdAndRoleAndStatus(
                        organizationId, currentUserId, OrganizationMemberRole.ADMIN, OrganizationMemberStatus.ACTIVE)) {
            throw new Exception("You are not allowed to add members to this organization");
        }

        User userToAdd = userRepository.findById(request.userId())
                .orElseThrow(() -> new Exception("User not found"));

        if (organizationMemberRepository.existsByOrganizationAndUser(organization, userToAdd)) {
            throw new Exception("User is already a member of this organization");
        }

        OrganizationMember member = new OrganizationMember();
        member.setOrganization(organization);
        member.setUser(userToAdd);
        member.setRole(OrganizationMemberRole.MEMBER);
        member.setStatus(OrganizationMemberStatus.ACTIVE);
        member.setJoinedAt(LocalDateTime.now());
        member.setUpdatedAt(LocalDateTime.now());

        OrganizationMember saved = organizationMemberRepository.save(member);
        return toOrganizationMemberResponseDto(saved);
    }


    @Transactional
    public void archiveOrganizationMember(Long organizationId, Long currentUserId, Long memberId) throws Exception {

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new Exception("Organization not found"));

        if (!Objects.equals(organization.getOwner().getId(), currentUserId) &&
                !organizationMemberRepository.existsByOrganizationIdAndUserIdAndRoleAndStatus(
                        organizationId, currentUserId, OrganizationMemberRole.ADMIN, OrganizationMemberStatus.ACTIVE)) {
            throw new Exception("You are not allowed to remove members from this organization");
        }

        OrganizationMember member = organizationMemberRepository.findById(memberId)
                .orElseThrow(() -> new Exception("Member not found"));

        if (!Objects.equals(member.getOrganization().getId(), organizationId)) {
            throw new Exception("Member does not belong to this organization");
        }

        if (Objects.equals(member.getUser().getId(), organization.getOwner().getId())) {
            throw new Exception("Cannot archive the organization owner");
        }

        if (member.getStatus() == OrganizationMemberStatus.ARCHIVED) {
            throw new Exception("Member is already archived");
        }

        member.setStatus(OrganizationMemberStatus.ARCHIVED);
        member.setUpdatedAt(LocalDateTime.now());
        organizationMemberRepository.save(member);
    }
}
