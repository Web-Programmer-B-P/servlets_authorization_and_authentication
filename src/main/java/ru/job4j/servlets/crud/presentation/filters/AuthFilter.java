package ru.job4j.servlets.crud.presentation.filters;

import ru.job4j.servlets.crud.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("*")
public class AuthFilter implements Filter {
    private static final String SING_IN_URI = "/sing-in";
    private static final String SING_IN_VIEW = "views/sing_in.html";
    private static final String JS_SING_IN = "js/sing_in.js";
    private static final String ATTRIBUTE_NAME_USER = "user";

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String currentRequestUri = req.getRequestURI();
        if (!(currentRequestUri.contains(SING_IN_URI) || currentRequestUri.contains(SING_IN_VIEW)
                || currentRequestUri.contains(JS_SING_IN))) {
            HttpSession session = req.getSession();
            if (session.isNew() || session.getAttribute(ATTRIBUTE_NAME_USER) == null) {
                resp.sendRedirect(String.format("%s%s", req.getContextPath(), SING_IN_URI));
                return;
            }
            User user = (User) session.getAttribute(ATTRIBUTE_NAME_USER);
            String role = user.getRoleName();
            if (role.equals("admin")) {
                chain.doFilter(req, resp);
                return;
            } else if (role.equals(ATTRIBUTE_NAME_USER) && req.getRequestURI().contains("page-user")
                    || req.getRequestURI().contains("/user_page.js") || req.getRequestURI().contains("/download")
                    || req.getRequestURI().contains("/list") || req.getRequestURI().contains("/upload")
                    || req.getRequestURI().contains("/city")) {
                chain.doFilter(req, resp);
                return;
            } else {
                resp.sendRedirect(String.format("%s%s", req.getContextPath(), SING_IN_URI));
                return;
            }
        }
        chain.doFilter(req, resp);
    }
}
