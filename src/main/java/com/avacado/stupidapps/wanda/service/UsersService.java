package com.avacado.stupidapps.wanda.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.avacado.stupidapps.wanda.domain.Users;
import com.avacado.stupidapps.wanda.repo.UsersRepo;
import com.avacado.stupidapps.wanda.utils.AvacadoUtils;

@Component
public class UsersService
{
  
  @Autowired
  UsersRepo usersRepo;
  
  BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(); 
  
  public Users saveUser(String email, String password) {
    if(usersRepo.existsById(email))
      return null;
    String encodedPassword = bCryptPasswordEncoder.encode(password);
    Users user = new Users();
    user.setUserName(email);
    user.setPassword(encodedPassword);
    user.setEnabled(1);
    String apiKey = getUniqueApiKey();
    user.setApiKey(apiKey);
    usersRepo.save(user);
    return user;
  }

  private String getUniqueApiKey()
  {
    String apiKey = AvacadoUtils.generateNewApiKey();
    Users existingUser = usersRepo.findByApiKey(apiKey);
    if(existingUser != null) {
      return getUniqueApiKey();
    }
    return apiKey;
  }

  public Users getCurrentUser()
  {
    String currentUserName = null;
    if(SecurityContextHolder.getContext().getAuthentication() != null &&
        SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
        !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
      Object currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if(currentPrincipal instanceof User) {
        currentUserName = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
      }
      else if(currentPrincipal instanceof Users) {
        currentUserName = ((Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserName();
      }
      return usersRepo.findById(currentUserName).get();
    }
    return null;
  }

  public Users findUserByUserNameAndPAssword(String userName, String password) {
    Optional<Users> user = usersRepo.findById(userName);
    if(user.isPresent()) {
      if(new BCryptPasswordEncoder().matches(password, user.get().getPassword())) {
        return user.get();
      }
    }
    return null;
  }

  public Users updateFcmToken(String fcmToken)
  {
    Users currentUser = usersRepo.findById(getCurrentUser().getUserName()).get();
    currentUser.setFcmToken(fcmToken);
    usersRepo.save(currentUser);
    return currentUser; 
  }

}
