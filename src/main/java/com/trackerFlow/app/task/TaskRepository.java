package com.trackerFlow.app.task;

import com.trackerFlow.app.project.Project;
import com.trackerFlow.app.project.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    boolean existsByProjectAndTitle(Project project, String title);
    List<Task>findByProject(Project project);
    List<Task> findByAssignee(ProjectMember assignee);
}
