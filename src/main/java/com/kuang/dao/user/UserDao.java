package com.kuang.dao.user;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.kuang.pojo.Role;
import com.kuang.pojo.User;

public interface UserDao {
	// 得到要登陆的用户
	public User getLoginUser(Connection connection, String userCode) throws Exception;

	// 修改当前密码
	public Integer updatePwd(Connection connection, int id, String password) throws Exception;

	// 查询用户总数
	public Integer getUserCount( Connection connection, String userName, int userRole ) throws Exception;

	// 获取用户列表
	public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize)throws Exception;


}
