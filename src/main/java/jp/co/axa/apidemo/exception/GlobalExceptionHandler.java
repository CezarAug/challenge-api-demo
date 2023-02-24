package jp.co.axa.apidemo.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import jp.co.axa.apidemo.infrastructure.rest.model.ErrorResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handling scenarios where the request is valid, but there is no data for that id.
   *
   * @param ex EmptyResultDataAccessException
   * @return Error body
   */
  @ExceptionHandler(value = {
      EmptyResultDataAccessException.class,
      EntityNotFoundException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  protected ErrorResponse handleIdNotFound(RuntimeException ex) {
    return new ErrorResponse(
        HttpStatus.BAD_REQUEST,
        ex.getMessage(),
        Arrays.asList("Unable to find an employee with the provided id.")
    );
  }

  /**
   * Handling invalid inputs errors, such as the value exceeds an Integer limit or invalid JSON.
   *
   * @param ex InputMismatchException
   * @return Error body
   */
  @ExceptionHandler(value = {
      InputMismatchException.class,
      HttpMessageNotReadableException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  protected ErrorResponse handleIInputMismatch(RuntimeException ex) {
    return new ErrorResponse(
        HttpStatus.BAD_REQUEST,
        ex.getMessage(),
        Collections.emptyList()
    );
  }

  /**
   * Handling general validation issues, such as nulls or length exceeded.
   *
   * @param ex MethodArgumentNotValidException
   * @return Error body
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    List<String> errorList = new ArrayList<>();

    result.getFieldErrors().forEach(fieldError -> errorList.add(formatFieldError(fieldError)));
    result.getGlobalErrors().forEach(globalError -> errorList.add(formatGlobalError(globalError)));

    return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), errorList);
  }


  /**
   * Handling enum validation issues.
   *
   * @param ex MethodArgumentTypeMismatchException
   * @return Error body
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorResponse handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
    return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(),
        Arrays.asList(String.format("%s: Received [%s]", ex.getName(), ex.getValue())));
  }

  /**
   * Handling other Illegal argument exceptions.
   *
   * @param ex IllegalArgumentException
   * @return Error body
   */
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
    return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), Collections.emptyList());
  }


  private String formatFieldError(FieldError fieldError) {
    return String.format("%s : %s. Received[%s]",
        fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue());
  }

  private String formatGlobalError(ObjectError globalError) {
    return String.format("%s : %s", globalError.getObjectName(), globalError.getDefaultMessage());
  }
}
