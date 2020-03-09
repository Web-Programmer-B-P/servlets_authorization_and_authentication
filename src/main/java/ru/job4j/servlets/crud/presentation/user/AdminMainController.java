package ru.job4j.servlets.crud.presentation.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.servlets.crud.logic.user.Validate;
import ru.job4j.servlets.crud.logic.user.ValidateUserService;
import ru.job4j.servlets.crud.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/admin-page")
public class AdminMainController extends HttpServlet {
    private static final Validate LOGIC = ValidateUserService.getInstance();
    private static final String ADMIN_HTML = "views/admin.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(String.format("%s%s", req.getContextPath(), ADMIN_HTML)).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<User> list = LOGIC.findAll();
        String responseObject = new ObjectMapper().writeValueAsString(list);
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");
        out.write(responseObject);
        out.flush();
    }
}
