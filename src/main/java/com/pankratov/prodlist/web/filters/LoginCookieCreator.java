/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.web.filters;

import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.http.*;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author pankratov
 */
public class LoginCookieCreator implements Filter {

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;
    private final static Logger log= org.apache.logging.log4j.LogManager.getLogger(LoginCookieCreator.class);
    public LoginCookieCreator() {
    }

    //Если пользователь авторизован, устанавливаем cookie с его login
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        try { 
            HttpServletResponse res =(HttpServletResponse) response;
            HttpServletRequest req =(HttpServletRequest) request;
            String login = ((HttpServletRequest) request).getRemoteUser();
             if (login != null) {
                Cookie c = new Cookie("login", URLEncoder.encode(login, "UTF-8"));
                c.setMaxAge(60 * 60 * 24 * 365 * 1);
                req.getSession().setAttribute("login", login);
                ((HttpServletResponse)response).addCookie(c);
            }
            chain.doFilter(request,response);      
        } catch (Throwable t) {
            log.error(t);
           throw new ServletException(t);
        }

    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
    }

    
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;

    }

}
