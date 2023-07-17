package com.leskiewicz.schoolsystem.service;

import com.leskiewicz.schoolsystem.dto.entity.UserDto;
import com.leskiewicz.schoolsystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    User getById(Long id);
    Page<User> getUsers(Pageable pageable);
    List<UserDto> toUserDtos(Page<User> usersPage);
}
