package com.kuang.service.user;

import com.kuang.pojo.User;

import java.sql.Connection;
import java.util.List;

public interface UserService {

    // 用户登陆
    public User login(String userCode, String password);

    //根据用户id修改密码
    public boolean updatePwd(int id, String pwd);

    //得到用户数量
    public int getUserCount(String username, int userRole);

    // 根据条件查新用户列表
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize);

    // 增加用户
    public boolean add(User user);

    // 删除用户
    public boolean deleteUserById(Integer delId);

    // 修改用户
    public User getUserById(String id);

    //根据userCode查询出User
    public User selectUserCodeExist(String userCode);


    // 修改用户信息
    public boolean modify(User user);

}
