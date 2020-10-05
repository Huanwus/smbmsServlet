package com.kuang.servlet.user;

import com.kuang.pojo.User;
import com.kuang.service.user.UserService;
import com.kuang.service.user.UserServiceImpl;
import com.kuang.util.Constants;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class LoginServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {


		//获取用户名和密码
		String userCode = req.getParameter("userCode");
		String userPassword = req.getParameter("userPassword");
		//调用service方法，进行用户匹配
		UserService userService = new UserServiceImpl();
		User user = userService.login(userCode,userPassword);
		if(null != user && userPassword.equals(user.getUserPassword()) ){//登录成功
			//放入session
			req.getSession().setAttribute(Constants.USER_SESSION, user);
			//页面跳转（frame.jsp）


			resp.sendRedirect("jsp/frame.jsp");
			System.out.println("进入frame");
		}else{
			//页面跳转（login.jsp）带出提示信息--转发
			req.setAttribute("error", "用户名或密码不正确");
			req.getRequestDispatcher("login.jsp").forward(req, resp);
		}


	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);

	}


}
