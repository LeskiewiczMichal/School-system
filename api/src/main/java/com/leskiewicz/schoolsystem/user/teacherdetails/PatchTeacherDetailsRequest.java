package com.leskiewicz.schoolsystem.user.teacherdetails;

import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
public record PatchTeacherDetailsRequest(
    String bio, String tutorship, DegreeTitle title, String degreeField) {}
