package ru.job4j.servlets.crud.presentation.autorisation;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.servlets.crud.logic.user.Validate;
import ru.job4j.servlets.crud.logic.user.ValidateUserService;
import ru.job4j.servlets.crud.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/sing-in")
public class SingInController extends HttpServlet {
    private static final Validate LOGIC = ValidateUserService.getInstance();
    private static final String PATH_TO_SING_IN_JSP = "views/sing_in.html";
    private static final String LOGIN_PARAMETER = "login";
    private static final String PASSWORD_PARAMETER = "password";
    private static final String ADMIN_PAGE = "/admin-page";
    private static final String USER_PAGE = "/page-user";
    private static final String KEY_RESPONSE = "message";
    private static final String CONTENT_TYPE = "text/html";
    private static final String MESSAGE_ERROR = "Пользователя с таким логином и паролем не найдено!";
    private static final String URL_KEY = "url";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(PATH_TO_SING_IN_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String> accumulateResponseMap = new HashMap<>();
        String login = req.getParameter(LOGIN_PARAMETER);
        String password = req.getParameter(PASSWORD_PARAMETER);
        if (LOGIC.isUserExist(login, password)) {
            User userFromStorage = LOGIC.findByLoginAndPassword(login, password);
            HttpSession session = req.getSession();
            session.setAttribute("user", userFromStorage);
            if (userFromStorage.getRoleName().equals("admin")) {
                accumulateResponseMap.put(URL_KEY, ADMIN_PAGE);
            } else {
                accumulateResponseMap.put(URL_KEY, USER_PAGE);
            }
        }
        buildResponse(resp, accumulateResponseMap);
    }

    private void buildResponse(HttpServletResponse resp, Map<String, String> responseMap) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType(CONTENT_TYPE);
        responseMap.put(KEY_RESPONSE, MESSAGE_ERROR);
        out.print(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(responseMap));
        out.flush();
    }
}
