package com.careerpath.domain.port;

import com.careerpath.domain.model.User;

public interface UserSyncPort {

    User syncUserFromExternal(String externalId, String email, boolean emailVerified);
}
