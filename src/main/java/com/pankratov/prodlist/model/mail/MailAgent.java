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
    final String HostName;
    final int HostPort;
    final String address;
    final String pwd;
    public MailAgent(ServletContext context) throws Exception{
        try{ 
          HostName=(String)context.getAttribute("DB_NAME");
          HostPort=465;//Integer.parseInt((String)context.getAttribute("appmailPort"));
          address=(String)context.getAttribute("appmailAddres");
          pwd=(String)context.getAttribute("appmail pwd");
        }catch(Exception e){ log.error("Ошибка при создании MailAgent",e);throw e;};
    }
    public  boolean sendMail(String hmsg,String msg){
        try{
        ImageHtmlEmail email = new ImageHtmlEmail();
        email.setHostName(HostName);
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("address", "pwd"));
        email.setSSLOnConnect(true);
        email.setFrom("address");
      
        URL url= new URL("127.0.0.1");
        email.setDataSourceResolver(new DataSourceUrlResolver( url) );
        email.setHtmlMsg(hmsg);
        email.setTextMsg("Hello from Lapla ");
        email.addTo("Pankratov_m@mail.ru");
        email.send();
        }catch(Exception e){}
    return false;
    }
    
}
