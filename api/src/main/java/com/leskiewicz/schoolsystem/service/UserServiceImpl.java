package com.leskiewicz.schoolsystem.service;

import com.leskiewicz.schoolsystem.dto.entity.UserDto;
import com.leskiewicz.schoolsystem.error.customexception.UserAlreadyExistsException;
import com.leskiewicz.schoolsystem.mapper.UserMapper;
import com.leskiewicz.schoolsystem.model.User;
import com.leskiewicz.schoolsystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with given id not found"));
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User with given email not found"));
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public List<UserDto> toUserDtos(Page<User> usersPage) {
        return usersPage.getContent().stream()
                .map(userMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void addUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        userRepository.save(user);
    }



}
