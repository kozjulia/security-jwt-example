package com.example.exception;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @Hidden
  public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
    List<String> stList = Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList();
    final List<ErrorResponse> errorResponses = e.getBindingResult().getFieldErrors().stream()
        .map(error -> {
          String mes = "Field: " + error.getField() + ". Error: " +
              error.getDefaultMessage() + " Value: " + error.getRejectedValue() + " ";
          return new ErrorResponse(mes);
        })
        .toList();
    StringBuilder stringBuilderInfos = new StringBuilder();
    StringBuilder stringBuilderErrors = new StringBuilder();
    for (ErrorResponse errorResponse : errorResponses) {
      stringBuilderInfos.append(errorResponse.getError().split("Value")[0].split("Error")[1]);
      stringBuilderErrors.append(errorResponse.getError());
    }

    ApiError apiError = new ApiError();
    apiError.setStatus(HttpStatus.BAD_REQUEST);
    apiError.setReason("Incorrectly made request.");
    apiError.setInfo(stringBuilderInfos.toString());
    apiError.setMessage(stringBuilderErrors.toString());
    apiError.setTimestamp(LocalDateTime.now());
    apiError.setErrors(stList);

    log.warn(apiError.toString());
    return apiError;
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @Hidden
  public ApiError handleNotSave(final AuthException e) {
    ApiError apiError = new ApiError();
    apiError.setStatus(HttpStatus.FORBIDDEN);
    apiError.setReason("For the requested operation the conditions are not met.");
    apiError.setInfo("Операция не может быть выполнена.");
    apiError.setMessage(e.getMessage());
    apiError.setTimestamp(LocalDateTime.now());
    apiError.setErrors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList());

    log.warn(apiError.toString());
    return apiError;
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @Hidden
  public ApiError handleMissingRequestParameter(final MissingServletRequestParameterException e) {
    ApiError apiError = new ApiError();
    apiError.setStatus(HttpStatus.BAD_REQUEST);
    apiError.setReason("Required request parameter is not present.");
    apiError.setInfo("Пропущен обязательный параметр.");
    apiError.setMessage(e.getMessage());
    apiError.setTimestamp(LocalDateTime.now());
    apiError.setErrors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList());

    log.warn(apiError.toString());
    return apiError;
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @Hidden
  public ApiError handleThrowable(final Throwable e) {
    ApiError apiError = new ApiError();
    apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    apiError.setReason("Произошла непредвиденная ошибка.");
    apiError.setInfo("Произошла непредвиденная ошибка.");
    apiError.setMessage(e.getMessage());
    apiError.setTimestamp(LocalDateTime.now());
    apiError.setErrors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList());

    log.warn(apiError.toString());
    return apiError;
  }

}