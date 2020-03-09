package ru.job4j.servlets.crud.presentation.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("*")
public class FilterEncoding implements Filter {
    private static final String CHARACTER_ENCODING = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(CHARACTER_ENCODING);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
