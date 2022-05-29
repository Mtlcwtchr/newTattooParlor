package by.bsuir.tattooparlor.controller;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Component
@Order(1)
public class InjectionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

        while (parameterNames.hasMoreElements())
        {
            String parameter = httpServletRequest.getParameter(parameterNames.nextElement());

            if(parameter.contains("<") || parameter.contains(">") || parameter.contains("</")) {
                ((HttpServletResponse)servletResponse).sendRedirect("/goFu");

                System.out.println("Injection caught with url: " + httpServletRequest.getRequestURL() + " uri: " + httpServletRequest.getRequestURI());
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
