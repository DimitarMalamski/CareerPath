package com.careerpath.interfaces.rest;

import com.careerpath.application.dto.SyncUserRequest;
import com.careerpath.application.service.UserSyncService;
import com.careerpath.domain.model.User;
import com.careerpath.interfaces.rest.dto.AuthSyncResponse;
import com.careerpath.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthSyncController {

    private final UserSyncService userSyncService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/sync")
    public ResponseEntity<AuthSyncResponse> syncUser(@RequestBody SyncUserRequest request) {
        User user = userSyncService.syncUserFromExternal(
                request.externalId(),
                request.email(),
                request.emailVerified()
        );

        String token = jwtTokenProvider.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );

        return ResponseEntity.ok(new AuthSyncResponse(token));
    }
}
