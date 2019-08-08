package com.avacado.stupidapps.wanda.controllers.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.avacado.stupidapps.wanda.configuration.UserAuthService;
import com.avacado.stupidapps.wanda.domain.Users;
import com.avacado.stupidapps.wanda.service.UsersService;

@Controller
public class WebHomeController
{
  
  @Autowired
  UsersService userService;
  
  @Autowired
  UserAuthService userAuthService;
  
  @PostMapping("/register")
  public String registerUser(HttpServletRequest request, @RequestParam("user_email") String email, @RequestParam("user_password") String password, @RequestParam("user_confirm_password") String confirmPassword) {
    if(password.equals(confirmPassword)) {
      Users user = userService.saveUser(email, password);
      if(user != null) {
        if(userAuthService.authUserForWebSessionByApiKey(user.getApiKey(), request)) {
          return "redirect:/account";
        }
      }
    }
    return "failed-register";
  }
  
  @GetMapping("/account")
  public ModelAndView getAuthPage() {
    ModelAndView mav = new ModelAndView("account");
    Users user = userService.getCurrentUser();
    mav.addObject("useremail", user.getUserName());
    mav.addObject("apiKey", user.getApiKey());
    if(user.getFcmToken() != null)
      mav.addObject("deviceRegistered", true);
    else
      mav.addObject("deviceRegistered", false);
    return mav;
  }
  
  @RequestMapping("/login")
  public String login() {
    if(userService.getCurrentUser() != null) {
      return "redirect:/account";
    }
    return "login";
  }
  
  @GetMapping("/logout")
  public String getLogoutPage(HttpServletRequest request, HttpServletResponse response) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null)
      new SecurityContextLogoutHandler().logout(request, response, authentication);
    return "redirect:/login";
  }

}
