package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserTest {

  @Mock private PasswordEncoder passwordEncoder;

  @Test
  public void updateUserChangesObjectProperly() {
    User user = User.builder().build();
    PatchUserRequest changeRequest =
        PatchUserRequest.builder().firstName("New first name").lastName("New last name").build();

    user.update(changeRequest, passwordEncoder);

    Assertions.assertEquals(changeRequest.firstName(), user.getFirstName());
    Assertions.assertEquals(changeRequest.lastName(), user.getLastName());
    Assertions.assertEquals(changeRequest.email(), user.getEmail());
    Assertions.assertEquals(changeRequest.password(), user.getPassword());
  }
}
