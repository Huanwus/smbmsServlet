package com.kuang.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.kuang.pojo.Role;
import com.kuang.pojo.User;
import com.kuang.service.Role.RoleService;
import com.kuang.service.Role.RoleServiceImpl;
import com.kuang.service.user.UserService;
import com.kuang.service.user.UserServiceImpl;
import com.kuang.util.Constants;
import com.kuang.util.PageSupport;
import com.mysql.jdbc.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 实现servlet复用
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method.equals("savepwd") && method != null){
            this.updatePwd(req, resp);
        } else if (method.equals("pwdmodify") && method != null){
            this.pwdModify(req, resp);
        } else if (method.equals("query") && method!=null){
            this.query(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }


    // 重点与难点
    public void query(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
       //查询用户列表
        // 从前端获取的
        String queryUserName = req.getParameter("queryname");  // userlist.jsp中根据名字查询
        String temp = req.getParameter("queryUserRole");   // userlist.jsp中根据用户的角色查询
        String pageIndex = req.getParameter("pageIndex");  // userlist.jsp中的页面数


        int queryUserRole = 0; // 角色默认为0,即管理员

        UserService userService = new UserServiceImpl();

        // 第一次走这个请求,一定是第一页, 页面大小是固定的
        //设置页面容量
        int pageSize = Constants.pageSize; // 页面容量就是5
        //当前页码是第一页
        int currentPageNo = 1;
        /**
         * http://localhost:8090/SMBMS/userlist.do
         * ----queryUserName --NULL
         * http://localhost:8090/SMBMS/userlist.do?queryname=
         * --queryUserName ---""
         */
//        System.out.println("queryUserName servlet--------"+queryUserName);
//        System.out.println("queryUserRole servlet--------"+queryUserRole);
//        System.out.println("query pageIndex--------- > " + pageIndex);

        //前端得到的queryUserName为空时
        if(queryUserName == null){
            queryUserName = "";
        }
        //下拉框时不能为空,即角色选择一个系统管理员,经理,普通员工
        if(temp != null && !temp.equals("")){
            queryUserRole = Integer.parseInt(temp);
        }

        if(pageIndex != null){
            try{
                currentPageNo = Integer.parseInt(pageIndex);
            }catch(NumberFormatException e){
                resp.sendRedirect("error.jsp");
            }
        }
        //获取用户的总数量(分页:上一页  下一页)
        int totalCount	= userService.getUserCount(queryUserName,queryUserRole);

        PageSupport pages=new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);

        // 总共有多少页
        int totalPageCount = pages.getTotalPageCount();

        //控制首页和尾页
        if(currentPageNo < 1){
            // 第一页
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            // 最后一页
            currentPageNo = totalPageCount;
        }

        //-------------------------以上都在准备数据-------下面进行数据展示-----------
        // 获取用户列表展示
        List<User> userList = null;
        userList = userService.getUserList(queryUserName,queryUserRole,currentPageNo, pageSize);
        req.setAttribute("userList", userList);
        // 获得角色列表
        List<Role> roleList = null;
        RoleService roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();
        req.setAttribute("roleList", roleList);


        req.setAttribute("queryUserName", queryUserName);
        req.setAttribute("queryUserRole", queryUserRole);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPageNo);
        req.getRequestDispatcher("userlist.jsp").forward(req, resp);

    }

    // 修改密码
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp){
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
        boolean flag = false;
        if (o!=null && newpassword != null){
            UserService userService = new UserServiceImpl();
            flag = userService.updatePwd(((User)o).getId(), newpassword);
            if (flag){
                req.setAttribute("message", "修改密码成功,请退出,重新登陆");
                req.getSession().removeAttribute(Constants.USER_SESSION);
            }else {
                req.setAttribute("message","新密码有问题");
            }

            try {
                req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //验证旧密码, session中有用户的旧密码
    public void pwdModify(HttpServletRequest req, HttpServletResponse resp){
        // 从session中拿id
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");
        // 万能的map:结果集
        Map<String, String> resultMap = new HashMap<String, String>();
        if (o==null){ // session失效
            resultMap.put("result","sessionerror");
        }else if (StringUtils.isNullOrEmpty(oldpassword)){// 输入的老密码为空
            resultMap.put("result","error");
        }else {
            String userPassword = ((User) o).getUserPassword();// session中用户的密码
            if (oldpassword.equals(userPassword)){
                resultMap.put("result","true");
            }else {
                resultMap.put("result","false");
            }
        }
        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            // JsonArray阿里巴巴工具类  转换格式
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
