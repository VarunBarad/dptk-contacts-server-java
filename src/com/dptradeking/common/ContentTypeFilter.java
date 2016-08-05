package com.dptradeking.common;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Creator: vbarad
 * Date: 2016-08-05
 * Project: DP-TradeKING
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
