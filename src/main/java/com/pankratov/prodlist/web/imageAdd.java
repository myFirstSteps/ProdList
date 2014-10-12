/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.web;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.util.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.*;
import org.json.simple.*;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author pankratov
 */
public class imageAdd extends HttpServlet {

    private long maxImgSize;
    private int maxMemSize;
    private Path temp;
    private Path imgDir;
    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(imageAdd.class);

    @Override
    public void init(ServletConfig config) {
        String param = null;
        ServletContext context = config.getServletContext();
        param = config.getServletContext().getInitParameter("MAX_UPLOAD_FILE_SIZE");
        Path appRoot = Paths.get(context.getRealPath(context.getContextPath()));
        maxImgSize = (param != null) ? Long.parseLong(param) : 512000;
        param = context.getInitParameter("MAX_FILE_MEMORY");
        maxMemSize = (param != null) ? Integer.parseInt(param) : 100 * 1024;
        param = context.getInitParameter("TEMP_FILE_DIR");
        temp = (param != null) ? Paths.get(param) : Paths.get(appRoot + "temp/imgfile");
        param = context.getInitParameter("PROD_IMG_FILE_DIR");
        imgDir = (param != null) ? Paths.get(param) : Paths.get(appRoot + "resources/img/products");
        if (!temp.isAbsolute()) {
            temp = Paths.get(appRoot + "/" + temp);
        }
        if (!imgDir.isAbsolute()) {
            imgDir = Paths.get(appRoot + "/" + imgDir);
        }
        try {
            Files.createDirectories(temp);
            Files.createDirectories(imgDir);
        } catch (IOException ex) {
            log.error("Ошибка  создания директории.", ex);
        }
        log.debug(String.format("Максимальный объем файла: %s\n Максимальный объем кеша: %s\n Директория временных файлов: %s\n Директория изображений: %s",
                maxImgSize, maxMemSize, temp, imgDir));
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println(request);
        ServletFileUpload.isMultipartContent(request);

        DiskFileItemFactory factory = new DiskFileItemFactory(maxMemSize, temp.toFile());
        ServletFileUpload upl = new ServletFileUpload(factory);
        upl.setFileSizeMax(maxImgSize);
        try {
            System.out.println(imgDir);

            File f = (Paths.get(imgDir + "/file1.jpg")).toFile();

            f.createNewFile();
            List<FileItem> x = upl.parseRequest(request);
            for (FileItem i : x) {
                if (!i.isFormField()) {
                    i.write(f);
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
