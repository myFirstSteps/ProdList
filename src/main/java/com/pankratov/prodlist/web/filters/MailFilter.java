/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.web.filters;

import com.pankratov.prodlist.model.mail.MailAgent;
import com.pankratov.prodlist.model.users.User;
import java.io.*;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

class MailHttpServletResponse extends HttpServletResponseWrapper {

    private StringWriter buff = new StringWriter();

    ;
     StringWriter getBuff() {
        return buff;
    }

    public MailHttpServletResponse(HttpServletResponse resp) {
        super(resp);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(buff);
    }
}

/**
 *
 * @author pankratov
 */
@WebFilter(filterName = "MailFilter", urlPatterns = {"/Registration"}, dispatcherTypes = {DispatcherType.REQUEST})
public class MailFilter implements Filter {

    private FilterConfig filterConfig = null;

    public MailFilter() {
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
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        Throwable problem = null;
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            if (req.getMethod().equals("POST")) {
                chain.doFilter(request, response);                  
                String mailType = (String) request.getAttribute("mailType");
                User user = (User) request.getAttribute("user");
                if (mailType != null && user != null && !user.getEmail().equals("")) {
                    MailHttpServletResponse resp = new MailHttpServletResponse((HttpServletResponse) response);
                    switch (mailType) {
                        case "registration":           
                            request.getRequestDispatcher("/WEB-INF/template/registrationMail.jsp").include(request, resp);
                            //System.out.println(resp.getBuff());
                            String alterMsg=String.format("Добро пожаловать %1$s!\nПоздравляем с успешной регистрацией.\n"
                                    + "Ваш логи:%s\nВаш пароль:%s",user.getLogin(),user.getPassword());
                            new MailAgent(getFilterConfig().getServletContext()).sendSingleMail(resp.getBuff().toString(),alterMsg,"registration",user.getEmail());
                            request.getRequestDispatcher("/WEB-INF/template/registrationMail.jsp").forward(request, response);
                        case "passrestore":;
                    }
                }
            } else {
                chain.doFilter(request, response);
            }
        } catch (Throwable t) {
            // If an exception is thrown somewhere down the filter chain,
            // we still want to execute our after processing, and then
            // rethrow the problem after that.
            problem = t;
            t.printStackTrace();
        }

    }

    /**
     * Return the filter configuration object for this filter.
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
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {

        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("MailFilter()");
        }
        StringBuffer sb = new StringBuffer("MailFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

}
