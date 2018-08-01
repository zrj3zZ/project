package com.iwork.core.filter;  
  
import java.io.IOException;  
import javax.servlet.Filter;  
import javax.servlet.FilterChain;  
import javax.servlet.FilterConfig;  
import javax.servlet.ServletException;  
import javax.servlet.ServletRequest;  
import javax.servlet.ServletResponse;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;
  
public class NewSessionFilter implements Filter {  
  
    public void destroy() {  
    }  
  
    public void doFilter(ServletRequest servletrequest, ServletResponse servletresponse, FilterChain filterchain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletrequest;
        HttpServletResponse response = (HttpServletResponse) servletresponse;
        String clientSessionId = servletrequest.getParameter("ssid");
        String serverSessionId = request.getSession().getId();
        if (serverSessionId.equals(clientSessionId)) {
            filterchain.doFilter(request, response);
        } else {
        	((HttpServletResponse) response).sendRedirect("/login.action");
			return;
        }
    }
  
    public void init(FilterConfig filterConfig) throws ServletException {  
    }  
  
}