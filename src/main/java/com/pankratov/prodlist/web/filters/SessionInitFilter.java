package com.pankratov.prodlist.web.filters;

import java.io.IOException;
import java.text.DateFormat;
import static java.text.DateFormat.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.logging.log4j.Logger;
/*Фильтр создает сессию (если она не создана). Ищет в cookie клиента «clid». Если «clid» найден, 
он присваивается новой сессии, в противном случае — создается новый «clid». 
«clid» - служит идентификатором неавторизованного пользователя, после авторизации
идентификатором выступает логин. Фильтр собирает историю посещенных пользователем страниц.*/
public class SessionInitFilter implements Filter {
    private final static Logger log= org.apache.logging.log4j.LogManager.getLogger(SessionInitFilter.class);
    private FilterConfig filterConfig = null;
    public class Client{
            String client;
            String role;
            Client(HttpServletRequest req){
                String temp;
                client= (temp=req.getRemoteUser())!=null?temp:(String)req.getSession().getAttribute("clid");
                role=req.isUserInRole("admin")?"admin":req.isUserInRole("level1")?"level1":"";
            }
            String getClient(){
                return client;
            }
            String getRole(){
                return role;
            }
        }
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
                Set<String> tempList;
                TreeMap<String,Set<String>> visited;
                try{
                visited=(tempVar=ses.getAttribute("visited"))!=null?
                        (TreeMap<String,Set<String>>)tempVar:new TreeMap<String,Set<String>>();
                tempList=(tempList=visited.get(req.getServletPath()))==null?new TreeSet<String>():tempList;
                tempList.add(DateFormat.getDateTimeInstance(SHORT,MEDIUM, Locale.getDefault()).format(new Date()));
                visited.put(req.getServletPath(), tempList);
                ses.setAttribute("visited", visited);}
                catch (Exception e){log.error(e);}
                Client cl=new Client(req);
                ses.setAttribute("client",cl.getClient());
                ses.setAttribute("role",cl.getRole());
                request.setCharacterEncoding("UTF-8");
                response.setCharacterEncoding("UTF-8");
                chain.doFilter(request, response);
               
            }
        } catch (Throwable t) { log.error(t);
           t.printStackTrace();
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
