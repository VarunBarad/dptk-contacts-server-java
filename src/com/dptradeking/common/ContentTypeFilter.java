package com.dptradeking.common;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by vbarad on 29/7/16.
 */
@WebFilter(filterName = "ContentTypeFilter")
public class ContentTypeFilter implements Filter {
  public void destroy() {
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
    response.setContentType("application/json");
    System.out.println("ContentType: " + request.toString());
    chain.doFilter(request, response);
  }

  public void init(FilterConfig config) throws ServletException {

  }

}
