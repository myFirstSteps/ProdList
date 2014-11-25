/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.web.filters;

import java.io.IOException;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author pankratov
 */
public class SessionInitFilter implements Filter {
    private final static Logger log= org.apache.logging.log4j.LogManager.getLogger(SessionInitFilter.class);
    private FilterConfig filterConfig = null;

    @SuppressWarnings("empty-statement")
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession ses= req.getSession(false);
        try {   
            if (ses==null) {
                boolean hasID=false;
                ses=req.getSession();
                 Cookie [] c= req.getCookies();
                 if (c!=null){
                 for(Cookie co:c){
                     if(co.getName().equals("clid")){
                        hasID=true;
                        ses.setAttribute("clid", co.getValue());
                     };
                 }
                 }
                 if (!hasID){Cookie id=new Cookie("clid","_clid"+ System.currentTimeMillis()+ses.getId());
                    id.setMaxAge(60*60*24*365);
                    ses.setAttribute("clid",id.getValue());
                    resp.addCookie(id);
                 }
                resp.sendRedirect(resp.encodeRedirectURL(req.getRequestURL().toString()));
            } else {
                Object tempVar;
                TreeMap<Long,String> visited;
                try{
                visited=(tempVar=ses.getAttribute("visited"))!=null?
                        (TreeMap<Long,String>)tempVar:new TreeMap<Long,String>();
                visited.put(System.currentTimeMillis(), req.getServletPath());
                ses.setAttribute("visited", visited);}
                catch (Exception e){log.error(e);}
                chain.doFilter(request, response);
               
            }
        } catch (Throwable t) { log.error(t);
            throw new ServletException(t);
        }
        ;

    }

  
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

 
    @Override
    public void destroy() {
    }

   
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;

    }

}
