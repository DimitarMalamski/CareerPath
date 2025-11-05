package com.careerpath.config;

import com.careerpath.model.User;
import com.careerpath.model.enums.UserRole;
import com.careerpath.repository.JobListingRepository;
import com.careerpath.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.CommandLineRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DevDatabaseSeederTest {
    @Test
    void testSeedDevDataCreatesUserAndJobsWhenEmpty() throws Exception {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        JobListingRepository jobListingRepository = mock(JobListingRepository.class);

        when(jobListingRepository.count()).thenReturn(0L);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DevDatabaseSeeder seeder = new DevDatabaseSeeder();
        CommandLineRunner runner = seeder.seedDevData(userRepository, jobListingRepository);

        // Act
        runner.run();

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
        verify(jobListingRepository, times(1)).saveAll(anyList());

        // Optional: verify recruiter role was set correctly
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getRole()).isEqualTo(UserRole.RECRUITER);
    }

    @Test
    void testSeedDevDataDoesNothingWhenJobsExist() throws Exception {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        JobListingRepository jobListingRepository = mock(JobListingRepository.class);
        when(jobListingRepository.count()).thenReturn(5L);

        DevDatabaseSeeder seeder = new DevDatabaseSeeder();
        CommandLineRunner runner = seeder.seedDevData(userRepository, jobListingRepository);

        // Act
        runner.run();

        // Assert
        verify(userRepository, never()).save(any(User.class));
        verify(jobListingRepository, never()).saveAll(anyList());
    }
}
