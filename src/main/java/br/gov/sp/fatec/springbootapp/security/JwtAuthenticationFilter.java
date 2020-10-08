package br.gov.sp.fatec.springbootapp.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class JwtAuthenticationFilter extends GenericFilterBean {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    try {
      HttpServletRequest servletRequest = (HttpServletRequest) request;
      String authorization = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);
      if (authorization != null) {
        Authentication credentials = JwtUtils.parseToken(authorization.replaceAll("Bearer ", ""));
        SecurityContextHolder.getContext().setAuthentication(credentials);
      }
      chain.doFilter(request, response);
    } catch (Throwable t) {
      HttpServletResponse servletResponse = (HttpServletResponse) response;
      servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, t.getMessage());
    }
  }

}