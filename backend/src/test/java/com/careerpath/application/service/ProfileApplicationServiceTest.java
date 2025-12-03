package com.careerpath.application.service;

import com.careerpath.application.dto.ProfileDto;
import com.careerpath.application.dto.ProfileSkillDto;
import com.careerpath.application.mapper.ProfileMapper;
import com.careerpath.domain.model.Profile;
import com.careerpath.domain.port.ProfilePersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileApplicationServiceTest {

    @Mock
    private ProfilePersistencePort profilePersistencePort;

    @InjectMocks
    private ProfileApplicationService profileApplicationService;

    @Test
    void getProfile_shouldReturnMappedProfileDto_whenProfileExists() {
        // Arrange
        String userId = "user123";

        Profile profile = Profile.builder()
                .userId(userId)
                .fullName("John Doe")
                .headline("Backend Dev")
                .location("Netherlands")
                .skills(List.of())
                .experiences(List.of())
                .build();

        when(profilePersistencePort.findByUserId(userId)).thenReturn(Optional.of(profile));

        // Act
        ProfileDto result = profileApplicationService.getProfile(userId);

        // Assert
        assertThat(result.fullName()).isEqualTo("John Doe");
        assertThat(result.headline()).isEqualTo("Backend Dev");

        verify(profilePersistencePort).findByUserId(userId);
    }

    @Test
    void getProfile_shouldThrowException_whenProfileNotFound() {
        // Arrange
        String userId = "missingUser";
        when(profilePersistencePort.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> profileApplicationService.getProfile(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Profile not found for user: " + userId);

        verify(profilePersistencePort).findByUserId(userId);
    }

    @Test
    void updateProfile_shouldUpdateFieldsAndReturnDto() {
        // Arrange
        String userId = "user123";

        Profile existing = Profile.builder()
                .userId(userId)
                .fullName("Old Name")
                .headline("Old Headline")
                .about("Old about")
                .location("Old City")
                .skills(List.of())
                .experiences(List.of())
                .aiOptIn(false)
                .build();

        List<ProfileSkillDto> skillDtos = List.of(
                new ProfileSkillDto("1", "Java", "advanced")
        );

        ProfileDto updateDto = new ProfileDto(
                "New Name",
                "New Headline",
                "New about",
                "New City",
                skillDtos,
                List.of(),
                true
        );

        when(profilePersistencePort.findByUserId(userId)).thenReturn(Optional.of(existing));

        Profile saved = ProfileMapper.toDomain(userId, updateDto);
        when(profilePersistencePort.save(any(Profile.class))).thenReturn(saved);

        // Act
        ProfileDto result = profileApplicationService.updateProfile(userId, updateDto);

        // Assert
        assertThat(result.fullName()).isEqualTo("New Name");
        assertThat(result.headline()).isEqualTo("New Headline");
        assertThat(result.about()).isEqualTo("New about");
        assertThat(result.location()).isEqualTo("New City");
        assertThat(result.skills())
                .extracting(ProfileSkillDto::id, ProfileSkillDto::name, ProfileSkillDto::level)
                .containsExactly(tuple("1", "Java", "advanced"));
        assertThat(result.aiOptIn()).isTrue();

        verify(profilePersistencePort).findByUserId(userId);
        verify(profilePersistencePort).save(any(Profile.class));
    }

    @Test
    void updateProfile_shouldThrowException_whenProfileNotFound() {
        // Arrange
        String userId = "missingUser";
        ProfileDto updateDto = new ProfileDto(
                "Name",
                "Headline",
                "About",
                "NL",
                List.of(),
                List.of(),
                false
        );

        when(profilePersistencePort.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> profileApplicationService.updateProfile(userId, updateDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Profile not found for user: " + userId);

        verify(profilePersistencePort).findByUserId(userId);
        verify(profilePersistencePort, never()).save(any());
    }
}
