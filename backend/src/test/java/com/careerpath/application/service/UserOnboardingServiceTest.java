package com.careerpath.application.service;

import com.careerpath.domain.model.Profile;
import com.careerpath.domain.port.ProfilePersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserOnboardingServiceTest {

    @Mock
    private ProfilePersistencePort profileRepositoryPort;

    @InjectMocks
    private UserOnboardingService userOnboardingService;

    @Test
    void ensureUserProfile_shouldDoNothing_whenProfileExists() {
        // Arrange
        String userId = "user123";
        when(profileRepositoryPort.existsByUserId(userId)).thenReturn(true);

        // Act
        userOnboardingService.ensureUserProfile(userId);

        // Assert
        verify(profileRepositoryPort).existsByUserId(userId);
        verify(profileRepositoryPort, never()).save(any(Profile.class));
    }

    @Test
    void ensureUserProfile_shouldCreateDefaultProfile_whenProfileDoesNotExist() {
        // Arrange
        String userId = "user123";
        when(profileRepositoryPort.existsByUserId(userId)).thenReturn(false);

        ArgumentCaptor<Profile> captor = ArgumentCaptor.forClass(Profile.class);

        // Act
        userOnboardingService.ensureUserProfile(userId);

        // Assert
        verify(profileRepositoryPort).existsByUserId(userId);
        verify(profileRepositoryPort).save(captor.capture());

        Profile saved = captor.getValue();

        assertThat(saved.getUserId()).isEqualTo(userId);
        assertThat(saved.getFullName()).isEmpty();
        assertThat(saved.getHeadline()).isEmpty();
        assertThat(saved.getAbout()).isEmpty();
        assertThat(saved.getLocation()).isEmpty();
        assertThat(saved.getSkills()).isEmpty();
        assertThat(saved.getExperiences()).isEmpty();
        assertThat(saved.isAiOptIn()).isTrue();
    }
}
