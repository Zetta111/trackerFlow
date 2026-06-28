package com.trackerFlow.app.task;

import com.trackerFlow.app.project.Project;
import com.trackerFlow.app.project.ProjectMember;
import com.trackerFlow.app.project.ProjectStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="Task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="task_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="project_id",nullable=false)
    private Project project;

    @ManyToOne
    @JoinColumn(name="project_member_id",nullable=false)
    private ProjectMember projectMember;

    @Column(name="description",nullable=true)
    private String description;

    @Column(name="title",nullable=false,unique = true)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name="status",nullable=false)
    private ProjectStatus status;

    @ManyToOne
    @JoinColumn(name="created_by_project_member",nullable=false)
    private ProjectMember createdBy;

    @ManyToOne
    @JoinColumn(name="assigned_project_member",nullable=false)
    private ProjectMember assignedMember;

    
}
