package com.dptradeking.common;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Creator: vbarad
 * Date: 2016-08-05
 * Project: DP-TradeKING
 */
@WebFilter(filterName = "AccessControlFilter")
public class AccessControlFilter implements Filter {
  public void destroy() {
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
    ((HttpServletResponse) response).setHeader("Access-Control-Allow-Origin", "*");
    ((HttpServletResponse) response).setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, HEAD, OPTIONS, PATCH");
    ((HttpServletResponse) response).setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    chain.doFilter(request, response);
  }

  public void init(FilterConfig config) throws ServletException {

  }

}
