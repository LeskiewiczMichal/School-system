package com.leskiewicz.schoolsystem.utils;

import com.leskiewicz.schoolsystem.security.Role;
import com.leskiewicz.schoolsystem.security.dto.AuthenticationRequest;
import com.leskiewicz.schoolsystem.security.dto.AuthenticationResponse;
import com.leskiewicz.schoolsystem.security.dto.RegisterRequest;
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
    return new AuthenticationRequest(StringUtils.maskEmail(request.getEmail()), "[PROVIDED]");
  }

  private static RegisterRequest maskRegisterRequest(RegisterRequest request) {
    return new RegisterRequest(StringUtils.maskInfo(request.getFirstName()),
        StringUtils.maskInfo(request.getLastName()), StringUtils.maskEmail(request.getEmail()),
        "[PROVIDED]", request.getFacultyName(), request.getDegreeField(), request.getDegreeTitle());
  }

  private static AuthenticationResponse maskAuthenticationResponse(
      AuthenticationResponse response) {
    // mask sensitive information in response object
    return new AuthenticationResponse(StringUtils.maskInfo(response.getToken()),
        LoggingUtils.maskUserDto(response.getUser()));
  }

  private static User maskUser(User user) {
    return new User(user.getId(), StringUtils.maskInfo(user.getFirstName()),
        StringUtils.maskInfo(user.getLastName()), StringUtils.maskEmail(user.getEmail()),
        "[PROVIDED]", user.getFaculty(), user.getDegree(), user.getRole());
  }

  private static UserDto maskUserDto(UserDto userDto) {
    return new UserDto(userDto.getId(), StringUtils.maskInfo(userDto.getFirstName()),
        StringUtils.maskInfo(userDto.getLastName()), StringUtils.maskEmail(userDto.getEmail()),
        userDto.getFaculty(), userDto.getDegree());
  }

  private static CollectionModel<?> maskCollectionModel(CollectionModel<?> collectionModel) {
    List<?> content = collectionModel.getContent().stream()
        .map(LoggingUtils::maskSensitiveInformation).collect(Collectors.toList());
    return CollectionModel.of(content, collectionModel.getLinks());
  }
}
