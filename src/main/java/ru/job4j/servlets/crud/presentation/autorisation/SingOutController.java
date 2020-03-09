package ru.job4j.servlets.crud.presentation.autorisation;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/sing-out")
public class SingOutController extends HttpServlet {
    private static final String SING_IN_URI = "/sing-in";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        if (!session.isNew()) {
            session.invalidate();
        }
        resp.sendRedirect(SING_IN_URI);
    }
}
