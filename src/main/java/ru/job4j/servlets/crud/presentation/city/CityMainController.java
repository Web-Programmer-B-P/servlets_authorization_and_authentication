package ru.job4j.servlets.crud.presentation.city;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.servlets.crud.logic.countries.CityStorage;
import ru.job4j.servlets.crud.logic.countries.ValidateCity;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/city")
public class CityMainController extends HttpServlet {
    private final static CityStorage LOGIC = ValidateCity.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String action = req.getParameter("action");
        if (action != null) {
            if (action.equals("all")) {
                sendResponse(new ObjectMapper().writeValueAsString(LOGIC.getAllCountry()), resp);
            }
            if (action.equals("cities") && id != null) {
                sendResponse(new ObjectMapper().writeValueAsString(LOGIC.getAllCity(Integer.parseInt(id))), resp);
            }
        }
    }

    private void sendResponse(String json, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");
        out.write(json);
        out.flush();
    }
}
