package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.faculty.dto.PatchFacultyRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
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

  @Nested
  public class equals {
    @Test
    public void returnsTrueForTheSameObject() {
      Assertions.assertTrue(faculty.equals(faculty));
    }

    @Test
    public void returnsFalseForNull() {
      Assertions.assertFalse(faculty.equals(null));
    }

    @Test
    public void returnsTrueForDifferentObjectsWithTheSameProperties() {
      Faculty firstFaculty = aFaculty().build();
      Faculty secondFaculty = aFaculty().build();

      Assertions.assertTrue(firstFaculty.equals(secondFaculty));
    }
  }

  @Nested
  public class hashCode {
    @Test
    public void returnsTheSameValueForTheSameObject() {
      Assertions.assertEquals(faculty.hashCode(), faculty.hashCode());
    }

    @Test
    public void returnsDifferentValuesForDifferentObjects() {
      Faculty firstFaculty = aFaculty().build();
      Faculty secondFaculty = aFaculty().name("OtherName").build();

      Assertions.assertNotEquals(firstFaculty.hashCode(), secondFaculty.hashCode());
    }
  }
}
