package com.leskiewicz.schoolsystem.faculty.utils;

import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.dto.UserDto;

public interface FacultyMapper {

    FacultyDto convertToDto(Faculty faculty);
}
