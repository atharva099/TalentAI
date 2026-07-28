package com.talentai.service.user;

import java.util.List;

import com.talentai.dto.request.UserRequest;
import com.talentai.dto.request.UserUpdateRequest;
import com.talentai.dto.response.UserResponse;

/**
 * Defines user lifecycle operations available to future API and authentication flows.
 */
public interface UserService {

    /**
     * Creates a user after validating uniqueness and securing the password.
     *
     * @param request validated user creation request
     * @return created user response
     */
    UserResponse createUser(UserRequest request);

    /**
     * Retrieves a user by its internal identifier.
     *
     * @param userId internal user identifier
     * @return user response
     */
    UserResponse getUserById(Long userId);

    /**
     * Retrieves all users.
     *
     * @return user responses
     */
    List<UserResponse> getAllUsers();

    /**
     * Updates a user's profile information.
     *
     * @param userId internal user identifier
     * @param request validated user profile update request
     * @return updated user response
     */
    UserResponse updateUser(Long userId, UserUpdateRequest request);

    /**
     * Deletes a user using the currently configured persistence strategy.
     *
     * @param userId internal user identifier
     */
    void deleteUser(Long userId);
}
