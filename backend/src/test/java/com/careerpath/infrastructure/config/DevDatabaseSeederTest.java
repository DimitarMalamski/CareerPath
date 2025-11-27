package com.careerpath.infrastructure.config;

import com.careerpath.domain.model.enums.UserRole;
import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataJobListingRepository;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.CommandLineRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DevDatabaseSeederTest {

    @Test
    void testSeedDevDataCreatesUserAndJobsWhenEmpty() throws Exception {
        // Arrange
        SpringDataUserRepository userRepository = mock(SpringDataUserRepository.class);
        SpringDataJobListingRepository jobListingRepository = mock(SpringDataJobListingRepository.class);

        when(jobListingRepository.count()).thenReturn(0L);
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DevDatabaseSeeder seeder = new DevDatabaseSeeder();
        CommandLineRunner runner = seeder.seedDevData(userRepository, jobListingRepository);

        // Act
        runner.run();

        // Assert
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(jobListingRepository, times(1)).saveAll(anyList());

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());

        assertThat(userCaptor.getValue().getRole()).isEqualTo(UserRole.RECRUITER);
        assertThat(userCaptor.getValue().getEmail()).isEqualTo("recruiter.dev@careerpath.com");
    }

    @Test
    void testSeedDevDataDoesNothingWhenJobsExist() throws Exception {
        // Arrange
        SpringDataUserRepository userRepository = mock(SpringDataUserRepository.class);
        SpringDataJobListingRepository jobListingRepository = mock(SpringDataJobListingRepository.class);

        when(jobListingRepository.count()).thenReturn(5L);

        DevDatabaseSeeder seeder = new DevDatabaseSeeder();
        CommandLineRunner runner = seeder.seedDevData(userRepository, jobListingRepository);

        // Act
        runner.run();

        // Assert
        verify(userRepository, never()).save(any(UserEntity.class));
        verify(jobListingRepository, never()).saveAll(anyList());
    }
}
