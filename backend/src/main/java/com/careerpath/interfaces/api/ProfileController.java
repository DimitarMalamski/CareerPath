package com.careerpath.interfaces.api;

import com.careerpath.application.dto.ProfileDto;
import com.careerpath.application.service.ProfileApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileApplicationService profileApplicationService;

    @GetMapping("/me")
    public ProfileDto getMyProfile(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return profileApplicationService.getProfile(userId);
    }

    @PutMapping("/me")
    public ProfileDto updateMyProfile(
            Authentication authentication,
            @RequestBody ProfileDto dto) {
        String userId = (String) authentication.getPrincipal();
        return profileApplicationService.updateProfile(userId, dto);
    }
}
