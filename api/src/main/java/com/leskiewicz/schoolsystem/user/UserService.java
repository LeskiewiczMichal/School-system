package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    User getById(Long id);
    Page<User> getUsers(Pageable pageable);
    List<UserDto> toUserDtos(Page<User> usersPage);
    void addUser(User user);
    User getByEmail(String email);
    User updateUser(PatchUserRequest request, Long userId);
}