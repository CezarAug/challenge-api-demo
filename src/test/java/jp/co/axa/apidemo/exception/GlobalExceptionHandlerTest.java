package jp.co.axa.apidemo.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.InputMismatchException;
import javax.persistence.EntityNotFoundException;
import jp.co.axa.apidemo.infrastructure.rest.controller.EmployeeController;
import jp.co.axa.apidemo.infrastructure.rest.model.EmployeeRequest;
import jp.co.axa.apidemo.infrastructure.rest.model.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

  private static final String ERROR_MSG = "Hi, I'm an exception";

  @Test
  void handleIdNotFound_EmptyResultDataAccessException() {
    EmptyResultDataAccessException ex = new EmptyResultDataAccessException(ERROR_MSG, 10);

    ErrorResponse errorResponse = globalExceptionHandler.handleIdNotFound(ex);

    assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getErrorCode());
    assertEquals(ERROR_MSG, errorResponse.getErrorMessage());
    assertEquals(Arrays.asList("Unable to find an employee with the provided id."),
        errorResponse.getErrors());
  }

  @Test
  void handleIdNotFound_EntityNotFoundException() {
    EntityNotFoundException ex = new EntityNotFoundException(ERROR_MSG);

    ErrorResponse errorResponse = globalExceptionHandler.handleIdNotFound(ex);

    assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getErrorCode());
    assertEquals(ERROR_MSG, errorResponse.getErrorMessage());
    assertEquals(Arrays.asList("Unable to find an employee with the provided id."),
        errorResponse.getErrors());
  }

  @Test
  void handleIdNotFound_EmptyMessage() {
    EntityNotFoundException ex = new EntityNotFoundException(null);

    ErrorResponse errorResponse = globalExceptionHandler.handleIdNotFound(ex);

    assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getErrorCode());
    assertEquals(null, errorResponse.getErrorMessage());
    assertEquals(Arrays.asList("Unable to find an employee with the provided id."),
        errorResponse.getErrors());
  }

  @Test
  void handleIInputMismatch_InputMismatchException() {
    InputMismatchException ex = new InputMismatchException(ERROR_MSG);

    ErrorResponse errorResponse = globalExceptionHandler.handleIInputMismatch(ex);

    assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getErrorCode());
    assertEquals(ERROR_MSG, errorResponse.getErrorMessage());
    assertEquals(Collections.emptyList(), errorResponse.getErrors());
  }

  @Test
  void handleIInputMismatch_HttpMessageNotReadableException() {
    HttpMessageNotReadableException ex = new HttpMessageNotReadableException(ERROR_MSG);

    ErrorResponse errorResponse = globalExceptionHandler.handleIInputMismatch(ex);

    assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getErrorCode());
    assertEquals(ERROR_MSG, errorResponse.getErrorMessage());
    assertEquals(Collections.emptyList(), errorResponse.getErrors());
  }

  @Test
  void handleMethodArgumentNotValidException() throws NoSuchMethodException {
    EmployeeRequest request = new EmployeeRequest();

    FieldError fieldError = new FieldError("employeeRequest", "name", "Testing field error");
    ObjectError globalError = new ObjectError("globalError", "Testing global error");

    MethodParameter methodParameter = new MethodParameter(
        EmployeeController.class.getMethod("getEmployee", Long.class), 0);

    BindingResult bindingResult = new BeanPropertyBindingResult(request, "objectName");
    bindingResult.addError(fieldError);
    bindingResult.addError(globalError);


    MethodArgumentNotValidException ex = new MethodArgumentNotValidException(
        methodParameter, bindingResult);

    ErrorResponse errorResponse = globalExceptionHandler.handleMethodArgumentNotValidException(ex);

    assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getErrorCode());
    assertTrue(errorResponse.getErrorMessage().contains("Validation failed for argument [0]"));
    assertEquals(
        Arrays.asList(
            "name : Testing field error. Received[null]",
            "globalError : Testing global error"
        ),
        errorResponse.getErrors());
  }

  @Test
  void handleTypeMismatchException() throws NoSuchMethodException {

    MethodParameter methodParameter = new MethodParameter(
        EmployeeController.class.getMethod("getEmployee", Long.class),
        0
    );

    MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
        "parameter",
        Integer.class,
        "Test name",
        methodParameter,
        new NumberFormatException("Invalid number")
    );
    ErrorResponse errorResponse = globalExceptionHandler.handleTypeMismatchException(ex);

    assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getErrorCode());
    assertTrue(errorResponse.getErrorMessage().contains("Failed to convert value"));
    assertEquals(Arrays.asList("Test name: Received [parameter]"),
        errorResponse.getErrors());
  }

  @Test
  void handleIllegalArgumentException() {
    IllegalArgumentException ex = new IllegalArgumentException(ERROR_MSG);

    ErrorResponse errorResponse = globalExceptionHandler.handleIllegalArgumentException(ex);

    assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getErrorCode());
    assertEquals(ERROR_MSG, errorResponse.getErrorMessage());
    assertEquals(Collections.emptyList(), errorResponse.getErrors());
  }
}