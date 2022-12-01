package ru.job4j.cinema.filter;

import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Класс фильтр
 * @author Burlakov
 */
@Component
public class AuthFilter implements Filter {

    private final Set<String> allowedLinks = Set.of("index", "fail", "login", "AddUser", "photoMovie",
            "registration", "loginPage", "logout", "formAddUser", "thisSessions", "theMovie");

    /**
     * проверка на вхождение страницы
     * @param uri url адреса
     * @return истина если найден, иначе ложь
     */
    public boolean allowed(String uri) {
        String[] split = uri.split("/");
        return split.length > 0
                && (allowedLinks.contains(split[split.length - 1])
                        || (split.length > 2 &&  allowedLinks.contains(split[split.length - 2])));
    }

    /**
     * фильтр
     * @param request ServletRequest
     * @param response ServletResponse
     * @param chain FilterChain
     * @throws IOException ошибка
     * @throws ServletException ошибка
     */
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        if (allowed(uri)) {
            chain.doFilter(req, res);
            return;
        }
        if (req.getSession().getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/loginPage");
            return;
        }
        chain.doFilter(req, res);
    }
}
