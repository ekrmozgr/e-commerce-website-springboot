package com.example.springproject;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "AuthFilter")
public class AuthorizationFilter implements Filter {

    public AuthorizationFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest reqt = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            HttpSession ses = reqt.getSession(false);

            String reqURI = reqt.getRequestURI();
            if(reqURI.equals("/"))
            {
                resp.sendRedirect(reqt.getContextPath() + "/index.xhtml");
            }
            else if ((ses != null && ses.getAttribute("username") != null))
            {
                if(reqURI.equals("/account.xhtml") || reqURI.equals("/basket.xhtml") || reqURI.equals("/orders.xhtml") || reqURI.equals("/order_details.xhtml"))
                {
                    chain.doFilter(request, response);
                }
                else
                {
                    boolean isAdmin = Boolean.valueOf(String.valueOf(ses.getAttribute("isAdmin")));
                    if(!isAdmin)
                    {
                        resp.sendRedirect(reqt.getContextPath() + "/acces_denied.xhtml");
                    }
                    chain.doFilter(request, response);
                }
            }
            else if(reqURI.equals("/create_user.xhtml"))
            {
                chain.doFilter(request, response);
            }
            else
                resp.sendRedirect(reqt.getContextPath() + "/login.xhtml");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void destroy() {

    }
}
