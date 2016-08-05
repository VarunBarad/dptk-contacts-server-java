package com.dptradeking.common;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by vbarad on 29/7/16.
 */
@WebFilter(filterName = "AccessControlFilter")
public class AccessControlFilter implements Filter {
  public void destroy() {
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
    ((HttpServletResponse) response).setHeader("Access-Control-Allow-Origin", "*");
    ((HttpServletResponse) response).setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, HEAD, OPTIONS, PATCH");
    ((HttpServletResponse) response).setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    System.out.println("AccessControl: " + request.toString());
    chain.doFilter(request, response);
  }

  public void init(FilterConfig config) throws ServletException {

  }

}
