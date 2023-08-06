package com.leskiewicz.schoolsystem.error;

import com.leskiewicz.schoolsystem.error.customexception.*;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@ControllerAdvice
public class DefaultExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    BindingResult result = ex.getBindingResult();
    List<FieldError> fieldErrors = result.getFieldErrors();
    String errorMessage =
        fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining("; "));
    ApiError apiError =
        new ApiError(
            request.getRequestURI(),
            errorMessage,
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now());
    return ResponseEntity.badRequest().body(apiError);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiError> handleIllegalArgumentException(
      IllegalArgumentException ex, HttpServletRequest request) {
    ApiError apiError =
        new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now());

    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiError> handleEntityNotFoundException(
      EntityNotFoundException ex, HttpServletRequest request) {
    ApiError apiError =
        new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now());

    return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
  }

  /// *** AUTHORIZATION *** ///

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ApiError> handleBadCredentialsException(
      BadCredentialsException ex, HttpServletRequest request) {
    ApiError apiError =
        new ApiError(
            request.getRequestURI(),
            "Incorrect email or password",
            HttpStatus.UNAUTHORIZED.value(),
            LocalDateTime.now());

    return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(value = {AccessDeniedException.class})
  public ResponseEntity<ApiError> handleJwtException(
      AccessDeniedException ex, HttpServletRequest request) {
    ApiError apiError =
        new ApiError(
            request.getRequestURI(),
            "Access denied",
            HttpStatus.FORBIDDEN.value(),
            LocalDateTime.now());

    return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(
      value = {
        MissingFieldException.class,
        UserAlreadyExistsException.class,
        FileUploadFailedException.class,
        MissingServletRequestPartException.class
      })
  public ResponseEntity<ApiError> handleMissingFieldException(
      RuntimeException ex, HttpServletRequest request) {
    ApiError apiError =
        new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now());

    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiError> handleArgumentMismatchException(
      RuntimeException ex, HttpServletRequest request) {
    ApiError apiError =
        new ApiError(
            request.getRequestURI(),
            "Wrong argument types provided",
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now());

    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(EntityAlreadyExistsException.class)
  public ResponseEntity<ApiError> handleEntityAlreadyExists(
      RuntimeException ex, HttpServletRequest request) {
    ApiError apiError =
        new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now());

    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(
      value = {DuplicateEntityException.class, EntitiesAlreadyAssociatedException.class})
  public ResponseEntity<ApiError> handleDuplicateEntityException(
      RuntimeException ex, HttpServletRequest request) {
    ApiError apiError =
        new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now());

    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleException(Exception ex, HttpServletRequest request) {
    ApiError apiError =
        new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now());

    return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
