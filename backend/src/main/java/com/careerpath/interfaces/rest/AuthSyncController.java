package com.careerpath.interfaces.rest;

import com.careerpath.application.dto.SyncUserRequest;
import com.careerpath.domain.port.UserSyncPort;
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

    private final UserSyncPort userSyncPort;

    @PostMapping("/sync")
    public ResponseEntity<Void> syncUser(@RequestBody SyncUserRequest request) {
        userSyncPort.syncUserFromExternal(
                request.externalId(),
                request.email(),
                request.emailVerified()
        );

        return  ResponseEntity.ok().build();
    }
}
