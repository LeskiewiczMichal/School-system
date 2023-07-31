package com.leskiewicz.schoolsystem.user.teacherdetails;

import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PatchTeacherDetailsRequest {

    private String bio;
    private String tutorship;
    private DegreeTitle title;
    private String degreeField;
}
