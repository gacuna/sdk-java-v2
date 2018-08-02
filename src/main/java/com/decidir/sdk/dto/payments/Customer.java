package com.decidir.sdk.dto.payments;

import java.io.Serializable;

public class Customer implements Serializable {

  private String id;
  private String email;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
