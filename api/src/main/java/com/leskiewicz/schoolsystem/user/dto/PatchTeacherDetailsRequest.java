package com.leskiewicz.schoolsystem.user.dto;

import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PatchTeacherDetailsRequest {

    private String bio;
    private String tutorship;
    private DegreeTitle title;
    private String degreeField;
}
