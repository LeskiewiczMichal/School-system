package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetails;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetailsModelAssembler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;

@ExtendWith(MockitoExtension.class)
public class TeacherDetailsModelAssemblerTest {

  @InjectMocks private TeacherDetailsModelAssembler teacherDetailsModelAssembler;

  @Test
  public void testToModelAddsCorrectLinks() {
    Faculty faculty = Mockito.mock(Faculty.class);
    User user = TestHelper.createTeacher(faculty);
    TeacherDetails teacherDetails = TestHelper.createTeacherDetails(user);

    Link selfLink = Link.of("/api/users/1/teacher-details");
    Link teacherLink = Link.of("/api/users/1");

    TeacherDetails result = teacherDetailsModelAssembler.toModel(teacherDetails);

    Assertions.assertEquals(selfLink.toUri(), result.getLink("self").get().toUri());
    Assertions.assertEquals(teacherLink.toUri(), result.getLink("teacher").get().toUri());
  }
}
