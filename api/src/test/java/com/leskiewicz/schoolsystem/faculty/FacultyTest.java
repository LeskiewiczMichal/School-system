package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.faculty.dto.PatchFacultyRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.leskiewicz.schoolsystem.builders.FacultyBuilder.aFaculty;

public class FacultyTest {

    Faculty faculty = aFaculty().build();

    @Test
    public void updateFacultyChangesObjectProperly() {
        Faculty faculty = aFaculty().build();
        PatchFacultyRequest changeRequest = new PatchFacultyRequest("New name");

        faculty.update(changeRequest);

        Assertions.assertEquals(changeRequest.name(), faculty.getName());
    }

    @Test
    public void toStringIsFormatted() {
        String expected = "Faculty{id=" + faculty.getId() + ", name='" + faculty.getName() + "'}";

        Assertions.assertEquals(expected, faculty.toString());
    }
}
