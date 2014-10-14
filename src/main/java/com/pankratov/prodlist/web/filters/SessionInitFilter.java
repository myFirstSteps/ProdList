/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.web.filters;

import java.io.IOException;
import java.util.*;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.*;

/**
 *
 * @author pankratov
 */
public class SessionInitFilter implements Filter {

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @SuppressWarnings("empty-statement")
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession ses= req.getSession(false);
        try {   
            if (ses==null) {
                boolean hasId=false;
                ses=req.getSession();
                 Cookie [] c= req.getCookies();
                 if (c!=null){
                 for(Cookie co:c){
                     if(co.getName().equals("clid")){
                        hasId=true;
                        ses.setAttribute("clid", co.getValue());
                     };
                 }
                 }
                 if (!hasId){Cookie id=new Cookie("clid","_clid"+ System.currentTimeMillis()+ses.getId());
                    id.setMaxAge(60*60*24*365);
                    ses.setAttribute("clid",id.getValue());
                    resp.addCookie(id);
                 }
                resp.sendRedirect(resp.encodeRedirectURL(req.getRequestURL().toString()));
            } else {
                chain.doFilter(request, response);
            }
        } catch (Throwable t) { System.out.println(t);
            
	    // If an exception is thrown somewhere down the filter chain,
            // we still want to execute our after processing, and then
            // rethrow the problem after that.
        }
        ;

    }

    /**
     * Return the filter configuration object for this filter.
     *
     * @return
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
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
