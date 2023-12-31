package com.leskiewicz.schoolsystem.builders;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetails;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import com.leskiewicz.schoolsystem.user.utils.UserMapperImpl;
import com.leskiewicz.schoolsystem.utils.Mapper;
import jakarta.persistence.*;

import java.util.List;

public class UserBuilder {

    private Long id = 1L;
    private String firstName = "John";
    private String lastName = "Doe";
    private String email = "johndoe@example.com";
    private String password = "password";
    private Faculty faculty = new FacultyBuilder().build();
    private Degree degree = new DegreeBuilder().build();
    private TeacherDetails teacherDetails;
    private Role role = Role.ROLE_STUDENT;
    private String profilePictureName = "default.png";

    public static UserBuilder anUser() {
        return new UserBuilder();
    }

    public static UserDto userDtoFrom(User user) {
        Mapper<User, UserDto> userMapper = new UserMapperImpl();
        return userMapper.mapToDto(user);
    }

    public static List<User> createUserList() {
        return List.of(anUser().build(), anUser().firstName("Jane").lastName("Smith").email("testingemail@example.com").build());
    }

    public static List<UserDto> createUserDtoListFrom(List<User> users) {
        return List.of(userDtoFrom(users.get(0)), userDtoFrom(users.get(1)));
    }

    public UserBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder faculty(Faculty faculty) {
        this.faculty = faculty;
        return this;
    }

    public UserBuilder degree(Degree degree) {
        this.degree = degree;
        return this;
    }

    public UserBuilder teacherDetails(TeacherDetails teacherDetails) {
        this.teacherDetails = teacherDetails;
        return this;
    }

    public UserBuilder role(Role role) {
        this.role = role;
        return this;
    }

    public UserBuilder profilePictureName(String profilePictureName) {
        this.profilePictureName = profilePictureName;
        return this;
    }

    public User build() {
        return User.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .faculty(faculty)
                .degree(degree)
                .teacherDetails(teacherDetails)
                .role(role)
                .profilePictureName(profilePictureName)
                .build();
    }

}
