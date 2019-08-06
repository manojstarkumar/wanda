package com.avacado.stupidapps.wanda.configuration;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    Users user = usersRepo.findByApiKey(apiKey);
    if(user != null) {
      UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(user, user.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_BASIC"));
      SecurityContextHolder.getContext().setAuthentication(authReq);
      return true;
    }
    return false;
  }
}
