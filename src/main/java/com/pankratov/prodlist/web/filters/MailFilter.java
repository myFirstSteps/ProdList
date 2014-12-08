package com.pankratov.prodlist.web.filters;

import com.pankratov.prodlist.model.mail.MailAgent;
import com.pankratov.prodlist.model.users.User;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import org.apache.logging.log4j.*;

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

@WebFilter(filterName = "MailFilter", urlPatterns = {"/Registration"}, dispatcherTypes = {DispatcherType.REQUEST})
public class MailFilter implements Filter {
    public static final Logger log= LogManager.getLogger(MailFilter.class);

    private FilterConfig filterConfig = null;

    public MailFilter() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
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
                            ;
                            request.getRequestDispatcher("/WEB-INF/template/registrationMail.jsp").include(request, resp);

                            String alterMsg=String.format("Добро пожаловать %1$s!\nПоздравляем с успешной регистрацией.\n"
                                    + "Ваш логи:%s\nВаш пароль:%s",user.getLogin(),user.getPassword());
                            new MailAgent(getFilterConfig().getServletContext()).sendSingleMail(resp.getBuff().toString(),alterMsg,"registration",user.getEmail());
                            
                            request.getRequestDispatcher(resp.encodeURL("/registration.jsp")).forward(request, response);
                        case "passrestore":;
                    }
                }
            } else {
                chain.doFilter(request, response);
            }
        } catch (Throwable t) {
            System.out.println(t);
           log.error(t);  
        }

    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

   
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

  
    public void destroy() {
    }

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {

        }
    }
}
