package com.leskiewicz.schoolsystem.error;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class DefaultExceptionHandler {

//    @ExceptionHandler(value = { MethodArgumentNotValidException.class })

//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        BindingResult result = ex.getBindingResult();
//        List<FieldError> fieldErrors = result.getFieldErrors();
//        Map<String, String> errors = new HashMap<>();
//        for (FieldError fieldError : fieldErrors) {
//            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
//        }
//        return ResponseEntity.badRequest().body(errors);
//    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String errorMessage = "Invalid request body";
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) ex.getCause();
            String fieldName = invalidFormatException.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .collect(Collectors.joining("."));
            errorMessage = "Invalid value for field '" + fieldName + "'";
        }
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                errorMessage,
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        String errorMessage = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                errorMessage,
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(apiError);
    }

}
