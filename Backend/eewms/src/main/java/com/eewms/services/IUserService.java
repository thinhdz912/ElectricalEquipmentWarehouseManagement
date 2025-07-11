package com.eewms.services;

import com.eewms.entities.User;
import com.eewms.entities.Role;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> findAllUsers();

    Optional<User> findUserById(Long id);

    User saveUser(User user);

    User updateUser(Long id, User updatedUser);

    void deleteUser(Long id);

    void toggleEnabledStatus(Long id);

    boolean existsByUsername(String username);

    List<Role> getAllRoles();
}
