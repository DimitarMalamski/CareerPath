package com.careerpath.application.mapper;

import com.careerpath.application.dto.ProfileDto;
import com.careerpath.application.dto.UserDto;
import com.careerpath.domain.modelOld.Profile;
import com.careerpath.domain.modelOld.User;
import com.careerpath.domain.modelOld.enums.UserRole;

public class UserMapper {
    public static UserDto toDto(User user) {
        Profile profile = user.getProfile();
        return new UserDto(
                user.getId(),
                user.getEmail(),
                null,
                user.getRole(),
                profile != null ? new ProfileDto(
                        profile.getFirstName(),
                        profile.getLastName(),
                        profile.getHeadline(),
                        profile.getLocation()
                ) : null
        );
    }

    public static User fromDto(UserDto dto) {
        return User.builder()
                .email(dto.email())
                .passwordHash(dto.passwordHash())
                .role(dto.role() != null ? dto.role() : UserRole.SEEKER)
                .build();
    }
}
