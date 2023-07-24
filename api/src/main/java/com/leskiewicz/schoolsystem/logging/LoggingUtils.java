package com.leskiewicz.schoolsystem.logging;

import com.leskiewicz.schoolsystem.authentication.dto.AuthenticationRequest;
import com.leskiewicz.schoolsystem.authentication.dto.AuthenticationResponse;
import com.leskiewicz.schoolsystem.authentication.dto.RegisterRequest;
import com.leskiewicz.schoolsystem.authentication.utils.ValidationUtils;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.CollectionModel;

public class LoggingUtils {

  public static Object maskSensitiveInformation(Object obj) {
    if (obj instanceof AuthenticationRequest) {
      return maskAuthenticationRequest((AuthenticationRequest) obj);
    } else if (obj instanceof RegisterRequest) {
      return maskRegisterRequest((RegisterRequest) obj);
    } else if (obj instanceof AuthenticationResponse) {
      return maskAuthenticationResponse((AuthenticationResponse) obj);
    } else if (obj instanceof User) {
      return maskUser((User) obj);
    } else if (obj instanceof UserDto) {
      return maskUserDto((UserDto) obj);
    } else if (obj instanceof CollectionModel) {
      return maskCollectionModel((CollectionModel<?>) obj);
    } else {
      return obj;
    }
  }

  private static AuthenticationRequest maskAuthenticationRequest(AuthenticationRequest request) {
    return new AuthenticationRequest(LoggingUtils.maskEmail(request.getEmail()), "[PROVIDED]");
  }

  private static RegisterRequest maskRegisterRequest(RegisterRequest request) {
    return new RegisterRequest(
        LoggingUtils.maskInfo(request.getFirstName()),
        LoggingUtils.maskInfo(request.getLastName()),
        LoggingUtils.maskEmail(request.getEmail()),
        "[PROVIDED]",
        request.getFacultyName(),
        request.getDegreeField(),
        request.getDegreeTitle());
  }

  private static AuthenticationResponse maskAuthenticationResponse(
      AuthenticationResponse response) {
    // mask sensitive information in response object
    return new AuthenticationResponse(
        LoggingUtils.maskInfo(response.getToken()), LoggingUtils.maskUserDto(response.getUser()));
  }

  private static User maskUser(User user) {
    return new User(
        user.getId(),
        LoggingUtils.maskInfo(user.getFirstName()),
        LoggingUtils.maskInfo(user.getLastName()),
        LoggingUtils.maskEmail(user.getEmail()),
        "[PROVIDED]",
        user.getFaculty(),
        user.getDegree(),
        user.getRole());
  }

  private static UserDto maskUserDto(UserDto userDto) {
    UserDto.UserDtoBuilder maskedUserDto =
        UserDto.builder()
            .id(userDto.getId())
            .firstName(LoggingUtils.maskInfo(userDto.getFirstName()))
            .lastName(LoggingUtils.maskInfo(userDto.getLastName()))
            .email(LoggingUtils.maskEmail(userDto.getEmail()))
            .faculty(userDto.getFaculty())
            .facultyId(userDto.getFacultyId());

    // Degree might be null if user is not a student
    if (userDto.getDegree() != null) {
      maskedUserDto.degree(userDto.getDegree());
      maskedUserDto.degreeId(userDto.getDegreeId());
    }

    UserDto mappedUserDto = maskedUserDto.build();
    ValidationUtils.validate(mappedUserDto);

    return mappedUserDto;
  }

  private static CollectionModel<?> maskCollectionModel(CollectionModel<?> collectionModel) {
    List<?> content =
        collectionModel.getContent().stream()
            .map(LoggingUtils::maskSensitiveInformation)
            .collect(Collectors.toList());
    return CollectionModel.of(content, collectionModel.getLinks());
  }

  public static String maskEmail(String email) {
    int atIndex = email.indexOf("@");
    if (atIndex == -1) {
      return email;
    }
    String username = email.substring(0, atIndex);
    String domain = email.substring(atIndex);
    int usernameLength = username.length();
    if (usernameLength <= 2) {
      return email;
    }
    int numCharsToMask = (int) Math.ceil(usernameLength / 2.0);
    int maskStartIndex = (usernameLength - numCharsToMask) / 2;
    char[] maskedUsernameChars = username.toCharArray();
    for (int i = maskStartIndex; i < maskStartIndex + numCharsToMask; i++) {
      maskedUsernameChars[i] = '*';
    }
    String maskedUsername = new String(maskedUsernameChars);
    return maskedUsername + domain;
  }

  public static String maskInfo(String info) {
    if (info == null || info.length() <= 2) {
      return info;
    }
    int length = info.length();
    return info.substring(0, 1) + "*".repeat(length - 2) + info.substring(length - 1);
  }
}
