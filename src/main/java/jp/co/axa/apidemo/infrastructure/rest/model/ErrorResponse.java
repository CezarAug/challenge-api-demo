package jp.co.axa.apidemo.infrastructure.rest.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ErrorResponse {

  @Getter
  private int errorCode;

  @Getter
  private String error;

  @Getter
  private String errorMessage;

  @Getter
  private List<String> errors = new ArrayList<>();

  public ErrorResponse(HttpStatus status, String message, List<String> errors) {
    this.errorCode = status.value();
    this.error = status.name();
    this.errorMessage = message;
    this.errors = errors;
  }
}
