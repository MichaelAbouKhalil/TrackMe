package com.trackme.projectservice.service;

import com.trackme.models.exception.NotFoundException;
import com.trackme.models.project.ProjectEntity;
import com.trackme.projectservice.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProjectDbService {

    private final ProjectRepository projectRepository;

    /**
     * retrieve project from database
     * @param projectId
     * @return
     */
    public ProjectEntity findProject(Long projectId){
        log.info("locating project with id [{}]", projectId);
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new NotFoundException("project with id [" + projectId + "] is not found."));

        return project;
    }

    /**
     * save project in database
     * @param project
     * @return
     */
    public ProjectEntity saveProject(ProjectEntity project){
        log.info("saving project with name [{}]", project.getName());
        ProjectEntity savedProject = projectRepository.save(project);

        if (savedProject == null) {
            throw new RuntimeException("Failed to save project in database");
        }
        return savedProject;
    }

    /**
     * delete project in database
     * @param project
     * @return
     */
    public void deleteProject(ProjectEntity project){

        log.info("deleting project with id [{}] and name [{}]", project.getId(), project.getName());
        projectRepository.deleteById(project.getId());
    }

}
