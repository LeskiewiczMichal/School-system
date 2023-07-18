package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeService;
import com.leskiewicz.schoolsystem.error.MissingFieldException;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyService;
import com.leskiewicz.schoolsystem.error.UserAlreadyExistsException;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final DegreeService degreeService;
    private final FacultyService facultyService;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public User updateUser(PatchUserRequest request, Long userId) {
        User user = this.getById(userId);
        if (request.getDegreeField() != null && request.getDegreeTitle() != null) {
            Faculty userFaculty;
            // If user is changing faculty, validate the new one
            if (request.getFacultyName() != null) {
                userFaculty = facultyService.getByName(request.getFacultyName());
            } else {
                userFaculty = user.getFaculty();
            }
            Degree degree = degreeService.getByTitleAndFieldOfStudy(request.getDegreeTitle(), request.getDegreeField());
            facultyService.validateDegreeForFaculty(userFaculty, degree);
            user.setDegree(degree);
        }
        if ((request.getDegreeField() != null && request.getDegreeTitle() == null) || (request.getDegreeField() == null && request.getDegreeTitle() != null)) {
            throw new MissingFieldException("Required for degree: title and field of study");
        }
        if (request.getFacultyName() != null) {
            // TODO: add logic for user changing faculty
            Faculty faculty = facultyService.getByName(request.getFacultyName());
            Degree degree = user.getDegree();
            facultyService.validateDegreeForFaculty(faculty, degree);
            user.setFaculty(faculty);
        }
        if (request.getEmail() != null) {
            Optional<User> sameEmailUser = userRepository.findByEmail(request.getEmail());
            if (sameEmailUser.isPresent()) {
                throw new UserAlreadyExistsException();
            }
            user.setEmail(request.getEmail());
        }
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            user.setPassword(encodedPassword);
        }
        userRepository.save(user);
        return user;
    }

}
