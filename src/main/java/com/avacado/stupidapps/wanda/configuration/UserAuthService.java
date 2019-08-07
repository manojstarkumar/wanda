package com.avacado.stupidapps.wanda.configuration;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import com.avacado.stupidapps.wanda.domain.Users;
import com.avacado.stupidapps.wanda.repo.UsersRepo;

@Component
public class UserAuthService implements UserDetailsService
{
  
  @Autowired
  UsersRepo usersRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
  {
    Optional<Users> user = usersRepo.findById(username);
    if(user.isPresent()) {
      return new User(user.get().getUserName(), user.get().getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_BASIC"));
    }
    throw new UsernameNotFoundException(username);
  }
  
  public boolean authUserByApiKey(String apiKey) {
    Authentication auth = getAuthTokenByApiKey(apiKey);
    if(auth !=null) {
      SecurityContextHolder.getContext().setAuthentication(auth);
      return true;
    }
    return false;
  }
  
  private Authentication getAuthTokenByApiKey(String apiKey) {
    Users user = usersRepo.findByApiKey(apiKey);
    if(user != null) {
      return new UsernamePasswordAuthenticationToken(user, user.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_BASIC"));
    }
    return null;
  }
  
  public boolean authUserForWebSessionByApiKey(String apiKey, HttpServletRequest request) {
    Authentication auth = getAuthTokenByApiKey(apiKey);
    if(auth !=null) {
      request.getSession();
      ((UsernamePasswordAuthenticationToken) auth).setDetails(new WebAuthenticationDetails(request));
      request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
      SecurityContextHolder.getContext().setAuthentication(auth);
      return true;
    }
    return false;
  }
}
