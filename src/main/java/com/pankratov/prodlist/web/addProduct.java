/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pankratov.prodlist.web;

import com.pankratov.prodlist.model.dao.DAOFactory;
import com.pankratov.prodlist.model.dao.ProductDAO;
import com.pankratov.prodlist.model.dao.jdbc.JDBCProductDAO;
import com.pankratov.prodlist.model.products.Product;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author pankratov
 */
public class addProduct extends HttpServlet {

    private enum Error {

        FILE_SIZE_ERROR, FILE_TYPE_ERROR, FIELD_DATA_ERROR;
        String fieldName;

        public void setFieldName(String name) {
            fieldName = name;
        }

        public String getFieldName(String name) {
            return fieldName;
        }
    }
    private long maxImgSize;
    private int maxMemSize;
    private Path temp;
    private Path imgDir;
    private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(addProduct.class);

    /*Не получилось быстро найти готовый класс для проверки сигнатуры файла на соответствие заявленному типу,
     по этому решил написать свой.*/
    private static class CheckFileContent {
        /*Метод проверяет содержится ли в файле f "magic number" соответствующий требуемому формату файла.
         Если сигнатура файла соответствует формату gif,jpeg или png, метод возвращает true.
         */

        static boolean isValid(File f) throws IOException {
            boolean result = false;
            final byte[] gifMagic = {0x47, 0x49, 0x46, 0x38, 0x39, 0x61};
            final byte[] gifMagic87a = {0x47, 0x49, 0x46, 0x38, 0x37, 0x61};
            final byte[] pngMagic = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A};
            final byte[] jpegMagicBegin = {(byte) 0xff, (byte) 0xd8};
            final byte[] jpegMagicEnd = {(byte) 0xff, (byte) 0xd9};
            byte[] b = new byte[6];

            try (RandomAccessFile raf = new RandomAccessFile(f, "r");) {
                if (raf.read(b) != -1) {
                    if (Arrays.equals(b, gifMagic) || Arrays.equals(b, gifMagic87a) || Arrays.equals(b, pngMagic)) {
                        result = true;
                    } else {
                        if (Arrays.equals(Arrays.copyOf(b, 2), jpegMagicBegin)) {
                            byte[] b1 = new byte[2];
                            raf.seek(raf.length() - 2);
                            raf.read(b1);
                            if (Arrays.equals(b1, jpegMagicEnd)) {
                                result = true;
                            }
                        }

                    }
                }

            }
            return result;
        }
    }

    @Override
    public void init(ServletConfig config) {
        String param = null;
        ServletContext context = config.getServletContext();
        param = config.getServletContext().getInitParameter("MAX_UPLOAD_FILE_SIZE");
        Path appRoot = Paths.get(context.getRealPath(context.getContextPath())).getParent();
       
        maxImgSize = (param != null) ? Long.parseLong(param) : 512000;
        param = context.getInitParameter("MAX_FILE_MEMORY");
        maxMemSize = (param != null) ? Integer.parseInt(param) : 100 * 1024;
        param = context.getInitParameter("TEMP_FILE_DIR");
        temp = (param != null) ? Paths.get(param) : Paths.get(appRoot + "temp/imgfile");
        param = context.getInitParameter("PROD_IMG_FILE_DIR");
        imgDir = (param != null) ? Paths.get(param) : Paths.get(appRoot + "resources/img/products");
        System.out.println(appRoot);
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet addProduct</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet addProduct at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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
        try {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(DAOFactory.getProductDAOInstance(DAOFactory.DAOSource.JDBC, this.getServletContext()).readProductNames());

        } catch (Exception ex) {
            System.out.println(ex);
            throw new IOException(ex);
        }
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
        try {
            Product product = new Product();
            String creator=request.getRemoteUser()!=null?request.getRemoteUser():(String)request.getSession().getAttribute("clid");
            if (creator!=null) product.setAuthor(creator);
            if (!ServletFileUpload.isMultipartContent(request)) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/plain");
                response.getWriter().println("Использован ошибочный способ передачи данных формы. Данные должны передаваться в 'multipart/form-data'");
                return;
            }
            DiskFileItemFactory factory = new DiskFileItemFactory(maxMemSize, temp.toFile());
            ServletFileUpload upl = new ServletFileUpload(factory);
            upl.setFileSizeMax(maxImgSize);
            String creationTime=DateFormat.getTimeInstance(DateFormat.LONG).format(new  Date());
            File f = (Paths.get(imgDir +"/"+ request.getSession().getId()+"_"+creationTime)).toFile();

            List<FileItem> x = upl.parseRequest(request);

            for (FileItem i : x) {
                if (!i.isFormField()) {
                    if (i.getSize() > 0) {
                        f.createNewFile();
                        i.write(f);
                    }
                } else {
                    switch (i.getFieldName()) {
                        case "category":
                            product.setGroup(i.getString("UTF-8"));
                            break;
                        case "name":
                            product.setName(i.getString("UTF-8"));
                            break;
                        case "sub_name":
                            product.setSubname(i.getString("UTF-8"));
                            break;
                        case "produser":
                            product.setProducer(i.getString("UTF-8"));
                            break;
                        case "value":
                            product.setValue(new Float(i.getString("UTF-8")));
                            break;
                        case "units":
                            product.setValueUnits(i.getString("UTF-8"));
                            break;
                        case "price":
                            product.setPrice(new Float(i.getString("UTF-8")));
                            break;
                        case "comment":
                            product.setComment(i.getString("UTF-8"));
                            break;
                    }
                }   
            }
            
            try(ProductDAO pdao=DAOFactory.getProductDAOInstance(DAOFactory.DAOSource.JDBC, request.getServletContext() )){
                pdao.addProduct(product);
                pdao.addGroup(product.getGroup());////////////TEMPPPPPP!!!!!!!!!!!!
            }
            System.out.println(product);
            //Проверяем, что загруженный файл является gif,png или jpeg.          
            if (f.length() > 0 && !CheckFileContent.isValid(f)) {
                f.delete();
                sendError(Error.FILE_TYPE_ERROR, request, response);
                return;
            };
        } catch (Exception e) {
            if (e.toString().contains("FileSizeLimitExceededException")) {
                sendError(Error.FILE_SIZE_ERROR, request, response);
                return;
            }
            throw new ServletException(e);
        }
    }

    protected void sendError(Error err, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String text = "";

        switch (err) {
            case FILE_SIZE_ERROR:
                text = String.format("Превышен размер прикрепляемого изображения. Максимальный допустимый размер %d Кбайт.\n",
                        maxImgSize / Math.round(Math.pow(2, 10)));
                break;
            case FILE_TYPE_ERROR:
                text = "Ошибка формата прикрепленного файла. Прикрепляемые файлы должны быть jpeg, png или gif.";
        }
        request.setAttribute("error", text);
        request.getRequestDispatcher(response.encodeURL("newProduct.jsp")).forward(request, response);
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
