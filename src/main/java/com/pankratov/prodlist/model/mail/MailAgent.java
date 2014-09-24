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
import java.net.*;
import java.util.*;

/**
 *
 * @author pankratov
 */
public class MailAgent {
    final static Logger log=org.apache.logging.log4j.LogManager.getLogger(MailAgent.class);
    final String HostName;
    final int HostPort;
    final String address;
    final String pwd;
    public MailAgent(ServletContext context) throws Exception{
        try{ 
        HostName=(String)context.getAttribute("appmailHost");
        HostPort=(int)context.getAttribute("appmailPort");
        address=(String)context.getAttribute("appmailAddres");
        pwd=(String)context.getAttribute("appmail pwd");
        }catch(Exception e){ log.error("Ошибка при создании MailAgent",e);throw e;};
    }
    public boolean sendRegistrationMail(){
        Email semail = new SimpleEmail();
        semail.setHostName("smtp.mail.ru");
        semail.setSmtpPort(465);
        semail.setAuthenticator(new DefaultAuthenticator("resumeapp@mail.ru", "somepass"));
        semail.setSSLOnConnect(true);
       // semail.setFrom("resumeapp@mail.ru");
      //  semail.setMsg("<html><head></head><body><style type='text/css'>body { background-color:#f0f0f0; }<style><h1>Hello from <em>Lapla</em></h1> nice to see you </body></html>");
       // semail.addTo("Pankratov_m@mail.ru");
       // semail.send();
    return false;
    }
    
}
