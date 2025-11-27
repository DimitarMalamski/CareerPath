package com.careerpath.domain.port;

public interface UserSyncPort {

    void syncUserFromExternal(String externalId, String email, boolean emailVerified);
}
