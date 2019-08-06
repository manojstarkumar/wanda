package com.avacado.stupidapps.wanda.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.avacado.stupidapps.wanda.domain.Users;
import com.avacado.stupidapps.wanda.service.UsersService;

@Controller
public class WebHomeController
{
  
  @Autowired
  UsersService userService;
  
  @PostMapping("/register")
  public String registerUser(@RequestParam("user_email") String email, @RequestParam("user_password") String password, @RequestParam("user_confirm_password") String confirmPassword) {
    if(password.equals(confirmPassword)) {
      boolean status = userService.saveUser(email, password);
      if(status)
        return "register";
    }
    return "failed-register";
  }
  
  @GetMapping("/auth")
  public ModelAndView getAuthPage() {
    ModelAndView mav = new ModelAndView("auth");
    Users user = userService.getCurrentUser();
    mav.addObject("apiKey", user.getApiKey());
    return mav;
  }
  
  @RequestMapping("/login")
  public String login() {
    return "login";
  }
 
}
