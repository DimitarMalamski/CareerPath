package com.careerpath.infrastructure.adapters;

import com.careerpath.domain.model.Profile;
import com.careerpath.infrastructure.persistence.jpa.adapter.JpaProfileRepositoryAdapter;
import com.careerpath.infrastructure.persistence.jpa.entity.ProfileDataEmbeddable;
import com.careerpath.infrastructure.persistence.jpa.entity.ProfileEntity;
import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataProfileRepository;

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
class JpaProfileRepositoryAdapterTest {

    @Mock
    private SpringDataProfileRepository profileRepository;

    @InjectMocks
    private JpaProfileRepositoryAdapter adapter;

    @Test
    void findByUserId_shouldReturnMappedDomain_whenEntityExists() {
        // Arrange
        String userId = "user123";

        ProfileDataEmbeddable data = ProfileDataEmbeddable.builder()
                .fullName("John Doe")
                .headline("Backend Engineer")
                .about("About me")
                .location("Netherlands")
                .skills(List.of())
                .experiences(List.of())
                .build();

        ProfileEntity entity = ProfileEntity.builder()
                .userId(userId)
                .data(data)
                .aiOptIn(true)
                .build();

        when(profileRepository.findByUserId(userId))
                .thenReturn(Optional.of(entity));

        // Act
        Optional<Profile> result = adapter.findByUserId(userId);

        // Assert
        assertThat(result).isPresent();
        Profile profile = result.get();
        assertThat(profile.getUserId()).isEqualTo(userId);
        assertThat(profile.getFullName()).isEqualTo("John Doe");
        assertThat(profile.getLocation()).isEqualTo("Netherlands");
        assertThat(profile.isAiOptIn()).isTrue();

        verify(profileRepository).findByUserId(userId);
    }

    @Test
    void findByUserId_shouldReturnEmpty_whenEntityNotFound() {
        when(profileRepository.findByUserId("missing"))
                .thenReturn(Optional.empty());

        Optional<Profile> result = adapter.findByUserId("missing");

        assertThat(result).isEmpty();
        verify(profileRepository).findByUserId("missing");
    }

    @Test
    void save_shouldPersistAndReturnMappedDomain() {
        // Arrange
        Profile domain = Profile.builder()
                .userId("user123")
                .fullName("Jane Doe")
                .headline("Engineer")
                .about("Some text")
                .location("Germany")
                .skills(List.of())
                .experiences(List.of())
                .aiOptIn(true)
                .build();

        ProfileEntity savedEntity = ProfileEntity.builder()
                .userId("user123")
                .data(ProfileDataEmbeddable.builder()
                        .fullName("Jane Doe")
                        .headline("Engineer")
                        .about("Some text")
                        .location("Germany")
                        .skills(List.of())
                        .experiences(List.of())
                        .build())
                .aiOptIn(true)
                .build();

        when(profileRepository.save(any(ProfileEntity.class)))
                .thenReturn(savedEntity);

        // Act
        Profile result = adapter.save(domain);

        // Assert
        assertThat(result.getUserId()).isEqualTo("user123");
        assertThat(result.getFullName()).isEqualTo("Jane Doe");
        assertThat(result.getLocation()).isEqualTo("Germany");
        assertThat(result.isAiOptIn()).isTrue();

        verify(profileRepository).save(any(ProfileEntity.class));
    }

    @Test
    void existsByUserId_shouldReturnRepositoryResult() {
        when(profileRepository.existsByUserId("abc"))
                .thenReturn(true);

        boolean exists = adapter.existsByUserId("abc");

        assertThat(exists).isTrue();
        verify(profileRepository).existsByUserId("abc");
    }
}
