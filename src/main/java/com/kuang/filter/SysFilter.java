package com.kuang.filter;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kuang.pojo.User;

// 若用户为空就不能进入管理界面
public class SysFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest rq = (HttpServletRequest)request;
        HttpServletResponse rp = (HttpServletResponse)response;
        User userSession = (User)rq.getSession().getAttribute("userSession");
        if(null == userSession){
            rp.sendRedirect("/smbms/error.jsp");
        }else{
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }

}
