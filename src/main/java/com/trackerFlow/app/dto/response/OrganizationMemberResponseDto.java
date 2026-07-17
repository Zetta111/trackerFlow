package com.trackerFlow.app.dto.response;

import com.trackerFlow.app.organization.OrganizationMemberRole;
import com.trackerFlow.app.organization.OrganizationMemberStatus;

public record OrganizationMemberResponseDto (
        Long id,
        Long userId,
        String firstName,
        String lastName,
        OrganizationMemberStatus status,
        OrganizationMemberRole role


){}
