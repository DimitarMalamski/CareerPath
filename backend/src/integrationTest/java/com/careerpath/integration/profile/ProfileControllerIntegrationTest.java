package com.careerpath.integration.profile;

import com.careerpath.BaseIntegrationTest;
import com.careerpath.domain.port.AiJobMatcherPort;
import com.careerpath.infrastructure.persistence.jpa.entity.ProfileEntity;
import com.careerpath.infrastructure.persistence.jpa.entity.ProfileDataEmbeddable;
import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataProfileRepository;
import com.careerpath.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class ProfileControllerIntegrationTest extends BaseIntegrationTest {

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringDataProfileRepository profileRepository;

    @MockitoBean
    private AiJobMatcherPort aiJobMatcherPort;

    @Test
    void shouldReturnMyProfile() throws Exception {
        // Arrange
        String userId = "user-123";

        ProfileDataEmbeddable data = ProfileDataEmbeddable.builder()
                .location("Eindhoven")
                .build();

        ProfileEntity entity = ProfileEntity.builder()
                .userId(userId)
                .data(data)
                .aiOptIn(false)
                .build();

        profileRepository.save(entity);

        // Act & Assert
        mockMvc.perform(get("/api/profile/me")
                        .principal(new UsernamePasswordAuthenticationToken(userId, null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value("Eindhoven"));
    }
}
