package com.trackerFlow.app.dto.response;

import com.trackerFlow.app.organization.OrganizationStatus;



public record OrganizationResponseDto (

    Long id,
    String name,
    String description,
    OrganizationStatus status
){}

