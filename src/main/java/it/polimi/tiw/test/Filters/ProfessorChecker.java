package it.polimi.tiw.test.Filters;

import it.polimi.tiw.test.Beans.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProfessorChecker implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse res =(HttpServletResponse) servletResponse;
        HttpServletRequest req= (HttpServletRequest) servletRequest;
        User u= (User) req.getSession().getAttribute("user");
        String loginpath = req.getServletContext().getContextPath() + "/LoginPage.html";
        if (req.getSession().isNew() || u == null || u.isStudent()) {
            res.sendRedirect(loginpath);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
