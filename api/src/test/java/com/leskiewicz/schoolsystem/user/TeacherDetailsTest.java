package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.user.teacherdetails.PatchTeacherDetailsRequest;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.leskiewicz.schoolsystem.builders.TeacherDetailsBuilder.aTeacherDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TeacherDetailsTest {

  @Test
  public void updateTeacherDetailsSavesProperTeacherDetails() {
    TeacherDetails teacherDetails = aTeacherDetails().build();
    PatchTeacherDetailsRequest changeRequest =
        PatchTeacherDetailsRequest.builder()
            .bio("New bio")
            .title(DegreeTitle.DOCTOR)
            .tutorship("New tutorship")
            .degreeField("New degree field")
            .build();

    teacherDetails.update(changeRequest);

    Assertions.assertEquals(changeRequest.getBio(), teacherDetails.getBio());
    Assertions.assertEquals(changeRequest.getTutorship(), teacherDetails.getTutorship());
    Assertions.assertEquals(changeRequest.getDegreeField(), teacherDetails.getDegreeField());
    Assertions.assertEquals(changeRequest.getTitle(), teacherDetails.getTitle());
  }
}
