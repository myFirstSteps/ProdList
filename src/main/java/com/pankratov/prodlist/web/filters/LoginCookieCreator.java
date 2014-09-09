/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.web.filters;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.*;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 * @author pankratov
 */
public class LoginCookieCreator implements Filter {

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    public LoginCookieCreator() {
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    //Если пользователь авторизован, устанавливаем cookie с его login
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        try { 
            HttpServletResponse res =(HttpServletResponse) response;
            HttpServletRequest req =(HttpServletRequest) request;
            String login = ((HttpServletRequest) request).getRemoteUser();
             if (login != null) {
                Cookie c = new Cookie("login", login);
                c.setMaxAge(60 * 60 * 24 * 365 * 10);
                ((HttpServletResponse)response).addCookie(c);
            }
            chain.doFilter(request,response);      
        } catch (Throwable t) {

        }

    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;

    }

}
