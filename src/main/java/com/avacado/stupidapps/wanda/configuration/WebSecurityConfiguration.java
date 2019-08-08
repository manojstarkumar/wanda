package com.avacado.stupidapps.wanda.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter
{
  
  @Autowired
  UserAuthService userAuthService;
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
    .authorizeRequests()
    .antMatchers("/web/**", "/resources/**").permitAll()
    .antMatchers("/", "/login", "/register", "/rest/**").permitAll()
    .anyRequest().authenticated()
    .and()
    .formLogin()
      .loginPage("/login")
      .defaultSuccessUrl("/account")
      .permitAll()
      .and()
    .logout()
      .permitAll();
    http.exceptionHandling().accessDeniedPage("/403");
  }
  
  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers(HttpMethod.POST, "/register");
  }

  @Autowired
  public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userAuthService).passwordEncoder(new BCryptPasswordEncoder());
  }
}
