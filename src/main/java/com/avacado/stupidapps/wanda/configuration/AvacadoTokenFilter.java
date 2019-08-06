package com.avacado.stupidapps.wanda.configuration;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import com.avacado.stupidapps.wanda.utils.Constants;

public class AvacadoTokenFilter implements Filter
{

  UserAuthService userAuthService;

  public AvacadoTokenFilter(UserAuthService userAuthService)
  {
    this.userAuthService = userAuthService;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException
  {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;

    String apiKey = httpRequest.getHeader(Constants.AVACADO_AUTH_HEADER);
    if (apiKey != null)
    {
      if (userAuthService.authUserByApiKey(apiKey))
        chain.doFilter(request, response);
      else
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
    else
    {
      httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
      // httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "token missing");
    }
  }

}
