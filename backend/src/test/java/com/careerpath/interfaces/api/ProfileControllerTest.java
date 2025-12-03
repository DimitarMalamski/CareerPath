package com.careerpath.interfaces.api;

import com.careerpath.application.dto.ProfileDto;
import com.careerpath.application.dto.ProfileSkillDto;
import com.careerpath.application.service.ProfileApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProfileControllerTest {

    @Test
    void getMyProfile_shouldReturnProfileDto() throws Exception {
        // Arrange
        ProfileApplicationService mockService = mock(ProfileApplicationService.class);
        Authentication mockAuth = mock(Authentication.class);

        String userId = "user123";

        // Mock authentication principal
        when(mockAuth.getPrincipal()).thenReturn(userId);

        ProfileDto dto = new ProfileDto(
                "John Doe",
                "Backend Engineer",
                "About me",
                "Netherlands",
                List.of(new ProfileSkillDto("1", "Java", "advanced")),
                List.of(),
                true
        );

        when(mockService.getProfile(userId)).thenReturn(dto);

        ProfileController controller = new ProfileController(mockService);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        // Act & Assert
        mockMvc.perform(get("/api/profile/me")
                        .principal(mockAuth) // Inject authentication
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.headline").value("Backend Engineer"))
                .andExpect(jsonPath("$.skills[0].name").value("Java"))
                .andExpect(jsonPath("$.aiOptIn").value(true));

        verify(mockService, times(1)).getProfile(userId);
    }

    @Test
    void updateMyProfile_shouldUpdateAndReturnProfileDto() throws Exception {
        // Arrange
        ProfileApplicationService mockService = mock(ProfileApplicationService.class);
        Authentication mockAuth = mock(Authentication.class);

        String userId = "user123";

        when(mockAuth.getPrincipal()).thenReturn(userId);

        ProfileDto inputDto = new ProfileDto(
                "Updated Name",
                "Updated Headline",
                "Updated about",
                "Germany",
                List.of(new ProfileSkillDto("1", "Java", "advanced")),
                List.of(),
                true
        );

        when(mockService.updateProfile(userId, inputDto)).thenReturn(inputDto);

        ProfileController controller = new ProfileController(mockService);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        String requestBody = """
                {
                  "fullName": "Updated Name",
                  "headline": "Updated Headline",
                  "about": "Updated about",
                  "location": "Germany",
                  "skills": [
                    { "id": "1", "name": "Java", "level": "advanced" }
                  ],
                  "experiences": [],
                  "aiOptIn": true
                }
                """;

        // Act & Assert
        mockMvc.perform(put("/api/profile/me")
                        .principal(mockAuth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Updated Name"))
                .andExpect(jsonPath("$.headline").value("Updated Headline"))
                .andExpect(jsonPath("$.location").value("Germany"))
                .andExpect(jsonPath("$.skills[0].name").value("Java"));

        verify(mockService, times(1)).updateProfile(userId, inputDto);
    }
}
