package com.careerpath.infrastructure.config;

import com.careerpath.domain.model.enums.UserRole;
import com.careerpath.infrastructure.persistence.jpa.entity.UserEntity;
import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataJobListingRepository;
import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataUserRepository;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.CommandLineRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProdDatabaseSeederTest {

    @Test
    void testSeedProdDataCreatesUserAndJobsWhenEmpty() throws Exception {
        // Arrange
        SpringDataUserRepository userRepo = mock(SpringDataUserRepository.class);
        SpringDataJobListingRepository jobRepo = mock(SpringDataJobListingRepository.class);

        when(jobRepo.count()).thenReturn(0L);
        when(userRepo.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProdDatabaseSeeder seeder = new ProdDatabaseSeeder();
        CommandLineRunner runner = seeder.seedProdData(userRepo, jobRepo);

        // Act
        runner.run();

        // Assert
        verify(userRepo, times(1)).save(any(UserEntity.class));
        verify(jobRepo, times(1)).saveAll(anyList());

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepo).save(userCaptor.capture());

        UserEntity savedUser = userCaptor.getValue();

        assertThat(savedUser.getRole()).isEqualTo(UserRole.RECRUITER);
        assertThat(savedUser.getEmail()).isEqualTo("recruiter@careerpath.com");
    }

    @Test
    void testSeedProdDataDoesNothingWhenJobsExist() throws Exception {
        // Arrange
        SpringDataUserRepository userRepo = mock(SpringDataUserRepository.class);
        SpringDataJobListingRepository jobRepo = mock(SpringDataJobListingRepository.class);

        when(jobRepo.count()).thenReturn(5L);

        ProdDatabaseSeeder seeder = new ProdDatabaseSeeder();
        CommandLineRunner runner = seeder.seedProdData(userRepo, jobRepo);

        // Act
        runner.run();

        // Assert
        verify(userRepo, never()).save(any(UserEntity.class));
        verify(jobRepo, never()).saveAll(anyList());
    }
}
