package com.trackme.projectservice.controller;

import com.trackme.models.common.CommonResponse;
import com.trackme.models.enums.ProjectStatusEnum;
import com.trackme.models.payload.request.project.DeleteProjectRequest;
import com.trackme.models.payload.request.project.GetProjectRequest;
import com.trackme.models.payload.request.project.NewProjectRequest;
import com.trackme.models.payload.request.project.UpdateProjectRequest;
import com.trackme.models.project.ProjectEntity;
import com.trackme.projectservice.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProjectControllerTest extends BaseController {

    private static final String BASE_API = "/project";

    @MockBean
    ProjectService projectService;

    ProjectEntity project;
    GetProjectRequest getProjectRequest;
    NewProjectRequest newProjectRequest;
    UpdateProjectRequest updateProjectRequest;
    DeleteProjectRequest deleteProjectRequest;

    @BeforeEach
    void setUp() {
        project = ProjectEntity.builder()
                .name("test-proj").description("test-desc")
                .status(ProjectStatusEnum.OPEN_PROJECT.getName())
                .build();

        getProjectRequest = GetProjectRequest.builder().projectId(1L).build();
        newProjectRequest = NewProjectRequest.builder()
                .name("temp-proj").description("desc")
                .build();
        updateProjectRequest = UpdateProjectRequest.builder()
                .projectId(1L).newName("new-temp-proj")
                .description("new-desc").status(ProjectStatusEnum.CLOSED_PROJECT.getName())
                .build();
        deleteProjectRequest = DeleteProjectRequest.builder().projectId(1L).build();
    }

    @DisplayName("Request Validation Tests")
    @Nested
    class RequestValidationTests {

        @Test
        public void getProject_RequestValidation_Valid() throws Exception {
            when(projectService.getProject(any(GetProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(get(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(getProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.payload").exists());
        }

        @Test
        public void getProject_RequestValidation_Invalid() throws Exception {
            when(projectService.getProject(any(GetProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            getProjectRequest.setProjectId(null);

            mockMvc.perform(get(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(getProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void createProject_RequestValidation_Valid() throws Exception {
            when(projectService.createProject(any(NewProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(post(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void createProject_RequestValidation_Invalid() throws Exception {
            when(projectService.createProject(any(NewProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            newProjectRequest.setName(null);

            mockMvc.perform(post(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void updateProject_RequestValidation_Valid() throws Exception {
            when(projectService.updateProject(any(UpdateProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(put(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.payload").exists());
        }

        @Test
        public void updateProject_RequestValidation_Invalid() throws Exception {
            when(projectService.updateProject(any(UpdateProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            updateProjectRequest.setProjectId(null);

            mockMvc.perform(put(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void deleteProject_RequestValidation_Valid() throws Exception {
            when(projectService.deleteProject(any(DeleteProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(delete(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(deleteProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.payload").exists());
        }

        @Test
        public void deleteProject_RequestValidation_Invalid() throws Exception {
            when(projectService.deleteProject(any(DeleteProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            deleteProjectRequest.setProjectId(null);

            mockMvc.perform(delete(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(deleteProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }
    }

    @DisplayName("Get Project Access Tests")
    @Nested
    class GetProjectAccessTests {
        @Test
        public void getProject_NoAuth_Invalid() throws Exception {
            when(projectService.getProject(any(GetProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(get(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(getProjectRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void getProject_ADMIN_Valid() throws Exception {
            when(projectService.getProject(any(GetProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(get(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(getProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.payload").exists());
        }

        @Test
        public void getProject_PM_Valid() throws Exception {
            when(projectService.getProject(any(GetProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(get(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(getProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + pmToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.payload").exists());
        }

        @Test
        public void getProject_Dev_Valid() throws Exception {
            when(projectService.getProject(any(GetProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(get(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(getProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + devToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.payload").exists());
        }

        @Test
        public void getProject_Customer_Valid() throws Exception {
            when(projectService.getProject(any(GetProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(get(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(getProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + custToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.payload").exists());
        }
    }

    @DisplayName("Create Project Access Tests")
    @Nested
    class CreateProjectAccessTests {
        @Test
        public void CreateProject_NoAuth_Invalid() throws Exception {
            when(projectService.createProject(any(NewProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(post(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newProjectRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void createProject_ADMIN_Valid() throws Exception {
            when(projectService.createProject(any(NewProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(post(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void createProject_PM_Valid() throws Exception {
            when(projectService.createProject(any(NewProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(post(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + pmToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.payload").exists());
        }

        @Test
        public void createProject_Dev_Invalid() throws Exception {
            when(projectService.createProject(any(NewProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(post(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + devToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void createProject_Customer_Invalid() throws Exception {
            when(projectService.createProject(any(NewProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(post(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + custToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }
    }

    @DisplayName("Update Project Access Tests")
    @Nested
    class UpdateProjectAccessTests {
        @Test
        public void updateProject_NoAuth_Invalid() throws Exception {
            when(projectService.updateProject(any(UpdateProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(put(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateProjectRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void updateProject_ADMIN_Valid() throws Exception {
            when(projectService.updateProject(any(UpdateProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(put(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.payload").exists());
        }

        @Test
        public void updateProject_PM_Valid() throws Exception {
            when(projectService.updateProject(any(UpdateProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(put(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + pmToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.payload").exists());
        }

        @Test
        public void updateProject_Dev_Invalid() throws Exception {
            when(projectService.updateProject(any(UpdateProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(put(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + devToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void updateProject_Customer_Invalid() throws Exception {
            when(projectService.getProject(any(GetProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(put(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(getProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + custToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }
    }

    @DisplayName("Delete Project Access Tests")
    @Nested
    class DeleteProjectAccessTests {
        @Test
        public void deleteProject_NoAuth_Invalid() throws Exception {
            when(projectService.deleteProject(any(DeleteProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(delete(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(deleteProjectRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void deleteProject_ADMIN_Valid() throws Exception {
            when(projectService.deleteProject(any(DeleteProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(delete(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(deleteProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.payload").exists());
        }

        @Test
        public void deleteProject_PM_Valid() throws Exception {
            when(projectService.deleteProject(any(DeleteProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(delete(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(deleteProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + pmToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.payload").exists());
        }

        @Test
        public void deleteProject_Dev_Invalid() throws Exception {
            when(projectService.deleteProject(any(DeleteProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(delete(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(deleteProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + devToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void deleteProject_Customer_Invalid() throws Exception {
            when(projectService.deleteProject(any(DeleteProjectRequest.class)))
                    .thenReturn(CommonResponse.ok(project));

            mockMvc.perform(delete(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(deleteProjectRequest))
                    .header("AUTHORIZATION", "Bearer " + custToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }
    }

}