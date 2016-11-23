package com.l1p.interop.dfsp.directory.exception;

public class ValidationError {

  private String message;
  private Param params;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Param getParams() {
    return params;
  }

  public void setParams(Param params) {
    this.params = params;
  }
}
