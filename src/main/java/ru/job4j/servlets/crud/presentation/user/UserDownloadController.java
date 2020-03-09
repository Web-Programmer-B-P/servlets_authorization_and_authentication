package ru.job4j.servlets.crud.presentation.user;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/download")
public class UserDownloadController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        if (name != null) {
            resp.setContentType("image/png");
            resp.setHeader("Content-Disposition", "attachment; filename*=utf-8"
                    + "''" + java.net.URLEncoder.encode(name, StandardCharsets.UTF_8) + ";");
            File file = new File(UploadFileController.PATH_TO_SAVE_IMAGES + File.separator + name);
            try (FileInputStream in = new FileInputStream(file)) {
                resp.getOutputStream().write(in.readAllBytes());
            }
        }
    }
}
