package com.example.API.service.user;

import com.example.API.model.User;
import com.example.API.request.UserCreateRequest;
import com.example.API.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(UserCreateRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

}
