package com.l1p.interop.dfsp.directory.exception;

import java.util.List;

public class DirectoryError {

  private String id;
  private String message;
  private List<ValidationError> validationErrors;

  public DirectoryError(String errorMessageId, String errorMessage) {
    id = errorMessageId;
    message = errorMessage;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<ValidationError> getValidationErrors() {
    return validationErrors;
  }

  public void setValidationErrors(List<ValidationError> validationErrors) {
    this.validationErrors = validationErrors;
  }
}
