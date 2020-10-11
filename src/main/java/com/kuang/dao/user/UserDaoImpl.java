package com.kuang.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kuang.pojo.Role;
import com.mysql.jdbc.StringUtils;

import com.kuang.dao.BaseDao;
import com.kuang.pojo.User;
import com.sun.prism.PresentableState;
import sun.security.krb5.internal.crypto.RsaMd5CksumType;

/**
 * dao层抛出异常，让service层去捕获处理
 * @author Administrator
 *
 */
public class UserDaoImpl implements UserDao{


	// 得到要登陆的用户
	@Override
	public User getLoginUser(Connection connection, String userCode) throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement pstm = null;
		ResultSet rs = null;
		User user = null;
		if(null != connection){
			String sql = "select * from smbms_user where userCode=?";
			Object[] params = {userCode};
			rs = BaseDao.execute(connection, pstm, rs, sql, params);
			if(rs.next()){
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUserCode(rs.getString("userCode"));
				user.setUserName(rs.getString("userName"));
				user.setUserPassword(rs.getString("userPassword"));
				user.setGender(rs.getInt("gender"));
				user.setBirthday(rs.getDate("birthday"));
				user.setPhone(rs.getString("phone"));
				user.setAddress(rs.getString("address"));
				user.setUserRole(rs.getInt("userRole"));
				user.setCreatedBy(rs.getInt("createdBy"));
				user.setCreationDate(rs.getTimestamp("creationDate"));
				user.setModifyBy(rs.getInt("modifyBy"));
				user.setModifyDate(rs.getTimestamp("modifyDate"));
			}
			BaseDao.closeResource(null, pstm, rs);
		}
		return user;
	}

	// 修改当前用户的密码
	@Override
	public Integer updatePwd(Connection connection, int id, String password) throws Exception {
		PreparedStatement pstm = null;
		int execute = 0;
		if (connection != null){
			String sql = "update smbms_user set userPassword = ? where id = ?";
			Object params[] = {password, id};
			execute = BaseDao.execute(connection, pstm, sql	, params);
			BaseDao.closeResource(null,pstm, null);
		}
		return execute;
	}


	// 根据用户名或者角色查询用户总数
	@Override
	public Integer getUserCount(Connection connection, String userName, int userRole) throws Exception {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int count = 0;
		if (connection != null){
			StringBuffer sql = new StringBuffer();
			sql.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole = r.id");
			ArrayList<Object> list = new ArrayList<Object>();
			if (!StringUtils.isNullOrEmpty(userName)) {
				sql.append(" and u.userName like ?");
				list.add("%" + userName + "%");// index 0
			}
			if (userRole > 0) {
				sql.append(" and u.userRole = ?");
				list.add(userRole);// index 1
			}
			Object[] params = list.toArray();
			rs = BaseDao.execute(connection, pstm, rs, sql.toString(), params);
			if (rs.next()) {
				// 从结果集中获取数量
				count = rs.getInt("count");
			}
			BaseDao.closeResource(null, pstm, rs);
		}
		return count;
	}


	// 获取用户列表
	@Override
	public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<User> userList = new ArrayList<User>();
		if(connection != null){
			StringBuffer sql = new StringBuffer();
			sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");
			List<Object> list = new ArrayList<Object>();
			if(!StringUtils.isNullOrEmpty(userName)){
				sql.append(" and u.userName like ?");
				list.add("%"+userName+"%");
			}
			if(userRole > 0){
				sql.append(" and u.userRole = ?");
				list.add(userRole);
			}
			// 数据库中分页使用limit startIndex, pageSize
			// 0,5
			// 5,5
			// 10,5

			sql.append(" order by creationDate DESC limit ?,?");
			currentPageNo = (currentPageNo-1)*pageSize;
			list.add(currentPageNo);
			list.add(pageSize);

			Object[] params = list.toArray();
			rs = BaseDao.execute(connection, pstm, rs, sql.toString(), params);
			while(rs.next()){
				User _user = new User();
				_user.setId(rs.getInt("id"));
				_user.setUserCode(rs.getString("userCode"));
				_user.setUserName(rs.getString("userName"));
				_user.setGender(rs.getInt("gender"));
				_user.setBirthday(rs.getDate("birthday"));
				_user.setPhone(rs.getString("phone"));
				_user.setUserRole(rs.getInt("userRole"));
				_user.setUserRoleName(rs.getString("userRoleName"));
				userList.add(_user);
			}
			BaseDao.closeResource(null, pstm, rs);
		}
		return userList;
	}

	@Override
	public int add(Connection connection, User user) throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement pstm = null;
		int updateRows = 0;
		if (null != connection) {
			String sql = "insert into smbms_user (userCode,userName,userPassword," +
					"userRole,gender,birthday,phone,address,creationDate,createdBy) " +
					"values(?,?,?,?,?,?,?,?,?,?)";
			Object[] params = {user.getUserCode(), user.getUserName("userName"), user.getUserPassword(),
					user.getUserRole(), user.getGender(), user.getBirthday(),
					user.getPhone(), user.getAddress(), user.getCreationDate(), user.getCreatedBy()};
			updateRows = BaseDao.execute(connection, pstm, sql, params);
			BaseDao.closeResource(null, pstm, null);
		}
		return updateRows;
	}


	@Override
	public int deleteUserById(Connection connection, Integer delId) throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement pstm = null;
		int flag = 0;
		if (null != connection) {
			String sql = "delete from smbms_user where id=?";
			Object[] params = {delId};
			flag = BaseDao.execute(connection, pstm, sql, params);
			BaseDao.closeResource(null, pstm, null);
		}
		return flag;
	}

	@Override
	public User getUserById(Connection connection, String id) throws Exception {
		// TODO Auto-generated method stub
		User user = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		if (null != connection) {
			String sql = "select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.id=? and u.userRole = r.id";
			Object[] params = {id};
			rs = BaseDao.execute(connection, pstm, rs, sql, params);
			if (rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUserCode(rs.getString("userCode"));
				user.setUserName(rs.getString("userName"));
				user.setUserPassword(rs.getString("userPassword"));
				user.setGender(rs.getInt("gender"));
				user.setBirthday(rs.getDate("birthday"));
				user.setPhone(rs.getString("phone"));
				user.setAddress(rs.getString("address"));
				user.setUserRole(rs.getInt("userRole"));
				user.setCreatedBy(rs.getInt("createdBy"));
				user.setCreationDate(rs.getTimestamp("creationDate"));
				user.setModifyBy(rs.getInt("modifyBy"));
				user.setModifyDate(rs.getTimestamp("modifyDate"));
				user.setUserRoleName(rs.getString("userRoleName"));
			}
			BaseDao.closeResource(null, pstm, rs);
		}
		return user;
	}


	@Override
	public int modify(Connection connection, User user) throws Exception {
		// TODO Auto-generated method stub
		int flag = 0;
		PreparedStatement pstm = null;
		if (null != connection) {
			String sql = "update smbms_user set userName=?," +
					"gender=?,birthday=?,phone=?,address=?,userRole=?,modifyBy=?,modifyDate=? where id = ? ";
			Object[] params = {user.getUserName("userName"), user.getGender(), user.getBirthday(),
					user.getPhone(), user.getAddress(), user.getUserRole(), user.getModifyBy(),
					user.getModifyDate(), user.getId()};
			flag = BaseDao.execute(connection, pstm, sql, params);
			BaseDao.closeResource(null, pstm, null);
		}
		return flag;
	}

}
