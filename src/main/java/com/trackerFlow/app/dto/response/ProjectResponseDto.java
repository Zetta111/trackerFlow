package com.trackerFlow.app.dto.response;

import com.trackerFlow.app.project.ProjectStatus;

public record ProjectResponseDto (
        Long id, String name, String description, ProjectStatus status
        ){}