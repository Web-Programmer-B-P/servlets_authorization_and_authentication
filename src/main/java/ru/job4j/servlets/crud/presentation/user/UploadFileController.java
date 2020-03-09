package ru.job4j.servlets.crud.presentation.user;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.servlets.crud.persistent.db.DbUserStore;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@WebServlet("/upload")
public class UploadFileController extends HttpServlet {
    private static final Logger LOG = LogManager.getLogger(DbUserStore.class.getName());
    private static final int INDEX_OF_FIRST_ELEMENT_FROM_REQUEST = 0;
    protected static final String PATH_TO_SAVE_IMAGES = "images/";
    private static final String MESSAGE_LOG = "Смотри в метод загрузки файла";
    private static final String CONTENT_TYPE = "text/html";
    private String nameImage;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        File repository = (File) this.getServletConfig().getServletContext().getAttribute(ServletContext.TEMPDIR);
        factory.setRepository(repository);
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            FileItem item = upload.parseRequest(req).get(INDEX_OF_FIRST_ELEMENT_FROM_REQUEST);
            File targetFolderToSave = new File(PATH_TO_SAVE_IMAGES);
            if (!targetFolderToSave.exists()) {
                targetFolderToSave.mkdir();
            }
            if (!item.isFormField()) {
                nameImage = UUID.randomUUID() + item.getName();
                File file = new File(targetFolderToSave + File.separator + nameImage);
                try (FileOutputStream out = new FileOutputStream(file)) {
                    out.write(item.getInputStream().readAllBytes());
                }
            }
            PrintWriter out = resp.getWriter();
            resp.setContentType(CONTENT_TYPE);
            out.print(nameImage);
            out.flush();
        } catch (FileUploadException e) {
            LOG.error(MESSAGE_LOG, e);
        }
    }
}
