package com.avacado.stupidapps.wanda.service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avacado.stupidapps.wanda.domain.Users;
import com.avacado.stupidapps.wanda.utils.Constants;

@Service
public class FcmService
{
  
  @Autowired
  UsersService usersService;
  
  public boolean postToFcm(String destination, String textMessage) throws IOException {
    Users currentUser = usersService.getCurrentUser();
    textMessage = textMessage.substring(0, Math.min(textMessage.length(), 159));
    String postMessage = String.format(Constants.AVACADO_FCM_POST_STRING, currentUser.getFcmToken(), destination, textMessage);
    
    
    URL obj = new URL(Constants.FCM_POST_URL);
    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

    con.setRequestMethod("POST");
    con.setRequestProperty("Authorization", Constants.FCM_AUTH_STRING);
    con.setRequestProperty("Content-Type", "application/json");

    
    con.setDoOutput(true);
    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
    wr.writeBytes(postMessage);
    wr.flush();
    wr.close();

    int responseCode = con.getResponseCode();
    
    if(responseCode == 200) {
      return true;
    }
    
    return false;
  }

}
