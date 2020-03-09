package ru.job4j.servlets.crud.presentation.role;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.servlets.crud.logic.role.ValidateRole;
import ru.job4j.servlets.crud.model.Role;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/role")
public class RoleMainController extends HttpServlet {
    private final ValidateRole logic = ValidateRole.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Role> list = logic.findAllRoles();
        String responseObject = new ObjectMapper().writeValueAsString(list);
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");
        out.write(responseObject);
        out.flush();
    }
}
