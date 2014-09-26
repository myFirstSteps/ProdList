/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pankratov.prodlist.model.mail;
import org.apache.commons.mail.*;
import javax.servlet.*;
import org.apache.logging.log4j.*;
import java.io.*;
import java.net.URL;
//import java.net.*;
import java.util.*;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;

/**
 *
 * @author pankratov
 */
public class MailAgent {
    final static Logger log=org.apache.logging.log4j.LogManager.getLogger(MailAgent.class);
    final String HOST_NAME;
    final int HOST_PORT;
    final String ADDRESS;
    final String PWD;
    public MailAgent(ServletContext context) throws Exception{
        try{ 
        //toString() может показаться неуместным, но он нужен для проверки на null.  
          HOST_NAME=context.getInitParameter("APPMAIL_HOST").toString(); 
          HOST_PORT=Integer.parseInt(context.getInitParameter("APPMAIL_PORT").toString());
          ADDRESS=context.getInitParameter("APPMAIL_ADDRESS").toString();
          PWD=context.getInitParameter("APPMAIL_PWD").toString();
        }catch(Exception e){ log.error("Ошибка при создании MailAgent",e);throw e;};
       
    }
    public  boolean sendSingleMail(String htmlmsg,String altmsg,String subj,String to){
        try{
        ImageHtmlEmail email = new ImageHtmlEmail();
        email.setHostName( HOST_NAME);
        email.setSmtpPort(HOST_PORT);
        email.setAuthenticator(new DefaultAuthenticator( ADDRESS, PWD));
        email.setSSLOnConnect(true);
        email.setFrom(ADDRESS);
        email.setSubject(subj);
        System.out.println(altmsg);
        URL url= new URL("http://127.0.0.1/");
        email.setDataSourceResolver(new DataSourceUrlResolver( url) );
        email.setHtmlMsg(htmlmsg);
        email.setTextMsg(altmsg);
        email.addTo(to);
        email.send();
        }catch(Exception e){System.out.println(e);}
    return false;
    }
    
}
