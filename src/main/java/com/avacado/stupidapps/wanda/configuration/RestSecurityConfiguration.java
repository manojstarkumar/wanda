package com.avacado.stupidapps.wanda.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Order(1)
public class RestSecurityConfiguration extends WebSecurityConfigurerAdapter
{
  
  @Autowired
  UserAuthService userAuthService;
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
    .antMatcher("/rest/**").authorizeRequests()
    .anyRequest().authenticated()
    .and()
    .addFilterBefore(new AvacadoTokenFilter(userAuthService), UsernamePasswordAuthenticationFilter.class);
  }
  
  @Override
  public void configure(WebSecurity web) {
      web.ignoring().antMatchers("/rest/login");
  }

}
