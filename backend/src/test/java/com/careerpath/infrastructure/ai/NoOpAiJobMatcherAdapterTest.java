package com.careerpath.infrastructure.ai;

import com.careerpath.domain.model.JobMatchResult;
import com.careerpath.domain.model.Profile;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class NoOpAiJobMatcherAdapterTest {

    private final NoOpAiJobMatcherAdapter adapter =
            new NoOpAiJobMatcherAdapter();

    @Test
    void enhanceMatches_shouldReturnInputUnchanged() {
        Profile profile = new Profile();

        List<JobMatchResult> input = List.of(
                JobMatchResult.builder()
                        .jobListingId("job-1")
                        .score(0.5)
                        .build()
        );

        List<JobMatchResult> result = adapter.enhanceMatches(profile, input);

        assertThat(result).isSameAs(input);
    }
}
