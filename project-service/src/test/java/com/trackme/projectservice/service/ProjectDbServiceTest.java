package com.trackme.projectservice.service;

import com.trackme.models.exception.ProjectNotFoundException;
import com.trackme.models.project.ProjectEntity;
import com.trackme.projectservice.Base;
import com.trackme.projectservice.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class ProjectDbServiceTest extends Base {

    @Autowired
    ProjectDbService projectDbService;

    @MockBean
    ProjectRepository projectRepository;

    ProjectEntity project;

    @BeforeEach
    void setUp() {
        project = ProjectEntity.builder()
                .name("test-proj").id(1L)
                .build();
    }

    @DisplayName("Find Project Tests")
    @Nested
    class FindProjectTests {
        @Test
        public void findProject_Valid() {
            when(projectRepository.findById(any(Long.class)))
                    .thenReturn(Optional.of(project));

            ProjectEntity returnedProject = projectDbService.findProject(any(Long.class));

            assertEquals(project, returnedProject);
        }

        @Test
        public void findProject_Invalid() {
            when(projectRepository.findById(any(Long.class)))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ProjectNotFoundException.class,
                    () -> {
                        projectDbService.findProject(any(Long.class));
                    }
            );
        }
    }

    @DisplayName("Save Project Tests")
    @Nested
    class SaveProjectTests {
        @Test
        public void saveProject_Valid() {
            when(projectRepository.save(any(ProjectEntity.class)))
                    .thenReturn(project);

            ProjectEntity savedProject = projectDbService.saveProject(project);

            assertEquals(project, savedProject);
        }

        @Test
        public void saveProject_Invalid() {
            when(projectRepository.save(any(ProjectEntity.class)))
                    .thenReturn(null);

            assertThrows(
                    RuntimeException.class,
                    () -> {
                        projectDbService.saveProject(project);
                    }
            );
        }
    }

    @DisplayName("Delete Project Tests")
    @Nested
    class DeleteProjectsTests {
        @Test
        public void deleteProject_Valid() {
            doNothing().when(projectRepository).deleteById(any(Long.class));

            projectDbService.deleteProject(project);

        }
    }
}