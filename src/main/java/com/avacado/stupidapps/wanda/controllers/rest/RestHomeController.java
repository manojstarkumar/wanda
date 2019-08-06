package com.avacado.stupidapps.wanda.controllers.rest;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avacado.stupidapps.wanda.domain.Users;
import com.avacado.stupidapps.wanda.service.FcmService;
import com.avacado.stupidapps.wanda.service.UsersService;
import com.avacado.stupidapps.wanda.utils.Constants;

@RestController
@RequestMapping("/rest")
public class RestHomeController
{

  @Autowired
  UsersService userService;
  
  @Autowired
  FcmService fcmService;

  @GetMapping(path = "/login")
  public ResponseEntity<Object> login(HttpServletRequest httpServletRequest)
  {
    String authHeader = httpServletRequest.getHeader("Authorization");
    Map<String, String> response = new HashMap<String, String>();
    if (!StringUtils.isAllEmpty(authHeader))
    {
      try
      {
        authHeader = new String(Base64.getDecoder().decode(authHeader));
      }
      catch (IllegalArgumentException iae)
      {
        response.put("error", "invalid request");
        return new ResponseEntity<Object>(response, HttpStatus.UNAUTHORIZED);
      }
      authHeader = authHeader.substring(6);
      String[] userNamePassword = authHeader.split(":");
      Users user = userService.findUserByUserNameAndPAssword(userNamePassword[0], userNamePassword[1]);
      if (user != null)
      {
        response.put("userName", user.getUserName());
        response.put("key", user.getApiKey());
        return new ResponseEntity<Object>(response, HttpStatus.OK);
      }
    }
    response.put("error", "invalid credentials");
    return new ResponseEntity<Object>(response, HttpStatus.UNAUTHORIZED);
  }

  @PostMapping("/registerToken")
  public ResponseEntity<Object> registerToken(@RequestBody String requestBody)
  {
    Map<String, String> response = new HashMap<String, String>();
    try
    {
      JSONObject requestJson = new JSONObject(requestBody);
      String fcmToken = requestJson.getString(Constants.AVACADO_NEW_TOKEN);
      if (!StringUtils.isEmpty(fcmToken))
      {
        if (userService.updateFcmToken(fcmToken) != null)
        {
          response.put("result", "Registration success");
          return new ResponseEntity<Object>(response, HttpStatus.OK);
        }
      }
      response.put("error", "something went wrong");
      return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
    }
    catch (JSONException jsonException)
    {
      response.put("error", "invalid request");
      return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
    }
  }
  
  @PostMapping("/sendsms")
  public ResponseEntity<Object> sendSms(@RequestBody String requestBody) {
    Map<String, String> response = new HashMap<String, String>();
    try
    {
      JSONObject requestJson = new JSONObject(requestBody);
      String destination = requestJson.getString(Constants.AVACADO_DESTINATION);
      String textMessage = requestJson.getString(Constants.AVACADO_TEXT_MESSAGE);
      if(StringUtils.isEmpty(destination) || StringUtils.isEmpty(textMessage)) {
        response.put("error", "destination or text missing");
        return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
      }
      if(fcmService.postToFcm(destination, textMessage)) {
        response.put("result", "sms sent");
        return new ResponseEntity<Object>(response, HttpStatus.OK);
      }
      response.put("error", "sms failed");
      return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
    }
    catch(JSONException | IOException jsonException)
    {
      response.put("error", "invalid request");
      return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
    }
  }

}
