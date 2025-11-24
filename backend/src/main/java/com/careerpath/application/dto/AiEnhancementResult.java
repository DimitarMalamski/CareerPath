package com.careerpath.application.dto;

public record AiEnhancementResult (
    String jobId,
    int aiScore,
    String aiExplanation
) {}
