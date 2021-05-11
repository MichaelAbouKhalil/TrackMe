package com.trackme.projectservice.controller;

import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.project.AssignRemoveRequest;
import com.trackme.models.project.ProjectEntity;
import com.trackme.projectservice.Base;
import com.trackme.projectservice.service.ProjectAssignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProjectAssignControllerTest extends BaseController {

    private static final String BASE_ASSIGN = "/project/assign";
    private static final String BASE_REMOVE = "/project/remove";

    @MockBean
    ProjectAssignService projectAssignService;

    AssignRemoveRequest assignRemoveRequest;
    ProjectEntity projectEntity;

    @BeforeEach
    void setUp() {
        assignRemoveRequest = AssignRemoveRequest.builder()
                .projectId(1L).email("test@email.com").build();
        projectEntity = ProjectEntity.builder()
                .id(1L).build();
    }

    @DisplayName("Assign Access Tests")
    @Nested
    class AssignAccessTests{
        @Test
        public void projectAssign_NoAuth_Invalid() throws Exception{
            when(projectAssignService.assign(any(AssignRemoveRequest.class)))
                    .thenReturn(CommonResponse.ok(projectEntity));

            mockMvc.perform(post(BASE_ASSIGN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(assignRemoveRequest))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }
        @Test
        public void projectAssign_Admin_Valid() throws Exception{
            when(projectAssignService.assign(any(AssignRemoveRequest.class)))
                    .thenReturn(CommonResponse.ok(projectEntity));

            mockMvc.perform(post(BASE_ASSIGN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("AUTHORIZATION", "Bearer " + adminToken)
                    .content(objectMapper.writeValueAsString(assignRemoveRequest))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }
        @Test
        public void projectAssign_Pm_Valid() throws Exception{
            when(projectAssignService.assign(any(AssignRemoveRequest.class)))
                    .thenReturn(CommonResponse.ok(projectEntity));

            mockMvc.perform(post(BASE_ASSIGN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("AUTHORIZATION", "Bearer " + pmToken)
                    .content(objectMapper.writeValueAsString(assignRemoveRequest))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }
        @Test
        public void projectAssign_Dev_Invalid() throws Exception{
            when(projectAssignService.assign(any(AssignRemoveRequest.class)))
                    .thenReturn(CommonResponse.ok(projectEntity));

            mockMvc.perform(post(BASE_ASSIGN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("AUTHORIZATION", "Bearer " + devToken)
                    .content(objectMapper.writeValueAsString(assignRemoveRequest))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }
        @Test
        public void projectAssign_Cust_Invalid() throws Exception{
            when(projectAssignService.assign(any(AssignRemoveRequest.class)))
                    .thenReturn(CommonResponse.ok(projectEntity));

            mockMvc.perform(post(BASE_ASSIGN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("AUTHORIZATION", "Bearer " + custToken)
                    .content(objectMapper.writeValueAsString(assignRemoveRequest))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }
    }

    @DisplayName("Remove Access Tests")
    @Nested
    class RemoveAccessTests{
        @Test
        public void projectRemove_NoAuth_Invalid() throws Exception{
            when(projectAssignService.remove(any(AssignRemoveRequest.class)))
                    .thenReturn(CommonResponse.ok(projectEntity));

            mockMvc.perform(post(BASE_REMOVE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(assignRemoveRequest))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }
        @Test
        public void projectRemove_Admin_Valid() throws Exception{
            when(projectAssignService.remove(any(AssignRemoveRequest.class)))
                    .thenReturn(CommonResponse.ok(projectEntity));

            mockMvc.perform(post(BASE_REMOVE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("AUTHORIZATION", "Bearer " + adminToken)
                    .content(objectMapper.writeValueAsString(assignRemoveRequest))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }
        @Test
        public void projectRemove_Pm_Valid() throws Exception{
            when(projectAssignService.remove(any(AssignRemoveRequest.class)))
                    .thenReturn(CommonResponse.ok(projectEntity));

            mockMvc.perform(post(BASE_REMOVE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("AUTHORIZATION", "Bearer " + pmToken)
                    .content(objectMapper.writeValueAsString(assignRemoveRequest))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }
        @Test
        public void projectRemove_Dev_Invalid() throws Exception{
            when(projectAssignService.remove(any(AssignRemoveRequest.class)))
                    .thenReturn(CommonResponse.ok(projectEntity));

            mockMvc.perform(post(BASE_REMOVE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("AUTHORIZATION", "Bearer " + devToken)
                    .content(objectMapper.writeValueAsString(assignRemoveRequest))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }
        @Test
        public void projectRemove_Cust_Invalid() throws Exception{
            when(projectAssignService.remove(any(AssignRemoveRequest.class)))
                    .thenReturn(CommonResponse.ok(projectEntity));

            mockMvc.perform(post(BASE_REMOVE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("AUTHORIZATION", "Bearer " + custToken)
                    .content(objectMapper.writeValueAsString(assignRemoveRequest))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }
    }

}