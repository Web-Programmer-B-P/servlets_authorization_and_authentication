package ru.job4j.servlets.crud.presentation.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.servlets.crud.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/page-user")
public class SingleUserController extends HttpServlet {
    private static final String USER_PAGE_JSP = "views/user_page.html";
    private static final String CONTENT_TYPE = "text/html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(String.format("%s%s", req.getContextPath(), USER_PAGE_JSP)).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = (User) req.getSession().getAttribute("user");
        String responseJsonStringUser = objectMapper.writeValueAsString(user);
        PrintWriter out = resp.getWriter();
        resp.setContentType(CONTENT_TYPE);
        out.write(responseJsonStringUser);
        out.flush();
    }
}
