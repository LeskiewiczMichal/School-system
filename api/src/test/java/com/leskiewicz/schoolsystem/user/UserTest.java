package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.leskiewicz.schoolsystem.builders.UserBuilder.anUser;

@ExtendWith(MockitoExtension.class)
public class UserTest {

  @Mock private PasswordEncoder passwordEncoder;

  User user = anUser().build();

  @Test
  public void updateUserChangesObjectProperly() {
    User user = anUser().build();
    PatchUserRequest changeRequest =
        PatchUserRequest.builder().firstName("New first name").lastName("New last name").email("newemail@example.com").password("newPassword").build();

    user.update(changeRequest, passwordEncoder);

    Assertions.assertEquals(changeRequest.firstName(), user.getFirstName());
    Assertions.assertEquals(changeRequest.lastName(), user.getLastName());
    Assertions.assertEquals(changeRequest.email(), user.getEmail());
    Assertions.assertEquals(passwordEncoder.encode(changeRequest.password()), user.getPassword());
  }

  @Test
  public void toStringIsFormatted() {
    User user = anUser().build();
    String expected =
        "User{id="
            + user.getId()
            + ", firstName='"
            + user.getFirstName()
            + "', lastName='"
            + user.getLastName()
            + "', email='"
            + user.getEmail()
            + "', role="
            + user.getRole()
            + "}";

    Assertions.assertEquals(expected, user.toString());
  }

  @Nested
  public class equals {
    @Test
    public void returnsTrueForTheSameObject() {
      Assertions.assertTrue(user.equals(user));
    }

    @Test
    public void returnsFalseForNull() {
      Assertions.assertFalse(user.equals(null));
    }

    @Test
    public void returnsTrueForDifferentObjectsWithTheSameProperties() {
      User firstUser = User.builder().build();
      User secondUser = User.builder().build();

      Assertions.assertTrue(firstUser.equals(secondUser));
    }
  }

  @Nested
  public class hashCode {
    @Test
    public void returnsTheSameValueForTheSameObject() {
      Assertions.assertEquals(user.hashCode(), user.hashCode());
    }

    @Test
    public void returnsDifferentValuesForDifferentObjects() {
      User firstUser = User.builder().build();
      User secondUser = User.builder().firstName("differentName").build();

      Assertions.assertNotEquals(firstUser.hashCode(), secondUser.hashCode());
    }
  }
}
