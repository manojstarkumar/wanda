package com.avacado.stupidapps.wanda.utils;

public class Constants
{

  public static final String AVACADO_AUTH_HEADER = "api-key";
  public static final String AVACADO_REST_LOGIN = "/rest/login";
  public static final String AVACADO_NEW_TOKEN = "avacadoToken";
  public static final String AVACADO_DESTINATION = "destinationNumber";
  public static final String AVACADO_TEXT_MESSAGE = "textMessage";
  
  public static final String AVACADO_FCM_POST_STRING = "{\"to\": \"%s\",\"data\": {\"destinationNumber\": \"%s\",\"textMessage\": \"%s\"}}";
  public static final String FCM_AUTH_STRING = "";
  public static final String FCM_POST_URL = "https://fcm.googleapis.com/fcm/send";

}
