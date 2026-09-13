package com.talentai.service.user;

import com.talentai.dto.request.UserRequest;
import com.talentai.dto.request.UserUpdateRequest;
import com.talentai.dto.response.UserResponse;
import com.talentai.dto.response.CurrentUserResponse;
import com.talentai.entity.User;

/**
 * Performs explicit mappings between User API DTOs and the persistence entity.
 */
public final class UserMapper {

    private UserMapper() {
    }

    /**
     * Maps a user creation request to a new user entity.
     *
     * <p>Password hashing is deliberately performed by the service before persistence.</p>
     *
     * @param request validated user creation request
     * @return new user entity
     */
    public static User toEntity(UserRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    /**
     * Maps a user entity to a response that excludes authentication secrets.
     *
     * @param user persisted user entity
     * @return safe user response
     */
    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    /**
     * Maps a user to the safe current-user response, including persisted roles.
     *
     * @param user persisted user entity
     * @return current user response
     */
    public static CurrentUserResponse toCurrentUserResponse(User user) {
        return CurrentUserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(java.util.stream.Collectors.toUnmodifiableSet()))
                .build();
    }

    /**
     * Applies a validated profile update to an existing user entity.
     *
     * @param request validated profile update request
     * @param user persisted user entity to update
     */
    public static void updateEntity(UserUpdateRequest request, User user) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
    }
}
