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

class ProdDatabaseSeederTest {
    @Test
    void testSeedProdDataCreatesUserAndJobsWhenEmpty() throws Exception {
        // Arrange
        UserRepository userRepo = mock(UserRepository.class);
        JobListingRepository jobRepo = mock(JobListingRepository.class);

        when(jobRepo.count()).thenReturn(0L);
        when(userRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProdDatabaseSeeder seeder = new ProdDatabaseSeeder();
        CommandLineRunner runner = seeder.seedProdData(userRepo, jobRepo);

        // Act
        runner.run();

        // Assert
        verify(userRepo, times(1)).save(any(User.class));
        verify(jobRepo, times(1)).saveAll(anyList());

        // Capture recruiter to check role correctness
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getRole()).isEqualTo(UserRole.RECRUITER);
        assertThat(userCaptor.getValue().getEmail()).isEqualTo("recruiter@careerpath.com");
    }

    @Test
    void testSeedProdDataDoesNothingWhenJobsExist() throws Exception {
        // Arrange
        UserRepository userRepo = mock(UserRepository.class);
        JobListingRepository jobRepo = mock(JobListingRepository.class);
        when(jobRepo.count()).thenReturn(5L);

        ProdDatabaseSeeder seeder = new ProdDatabaseSeeder();
        CommandLineRunner runner = seeder.seedProdData(userRepo, jobRepo);

        // Act
        runner.run();

        // Assert
        verify(userRepo, never()).save(any(User.class));
        verify(jobRepo, never()).saveAll(anyList());
    }
}
