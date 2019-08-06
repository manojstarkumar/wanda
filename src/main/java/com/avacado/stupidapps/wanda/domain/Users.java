package com.avacado.stupidapps.wanda.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class Users
{
  @Id
  private String userName;
  private String password;
  private int enabled;
  private String apiKey;
  private String fcmToken;
  public String getUserName()
  {
    return userName;
  }
  public void setUserName(String userName)
  {
    this.userName = userName;
  }
  public String getPassword()
  {
    return password;
  }
  public void setPassword(String password)
  {
    this.password = password;
  }
  public int getEnabled()
  {
    return enabled;
  }
  public void setEnabled(int enabled)
  {
    this.enabled = enabled;
  }
  public String getApiKey()
  {
    return apiKey;
  }
  public void setApiKey(String apiKey)
  {
    this.apiKey = apiKey;
  }
  public String getFcmToken()
  {
    return fcmToken;
  }
  public void setFcmToken(String fcmToken)
  {
    this.fcmToken = fcmToken;
  }

}
