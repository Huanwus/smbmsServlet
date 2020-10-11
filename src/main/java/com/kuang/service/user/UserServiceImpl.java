package com.kuang.service.user;

import com.kuang.dao.BaseDao;
import com.kuang.dao.user.UserDao;
import com.kuang.dao.user.UserDaoImpl;
import com.kuang.pojo.User;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserServiceImpl implements UserService{
    // 引入Dao
    private UserDao userDao;
    public UserServiceImpl(){
        userDao = new UserDaoImpl();
    }

    @Override
    public User login(String userCode, String userPassword) {
        // TODO Auto-generated method stub
        Connection connection = null;
        User user = null;
        try {
            connection = BaseDao.getConnection();
            user = userDao.getLoginUser(connection, userCode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            BaseDao.closeResource(connection, null, null);
        }


        return user;
    }

    @Override
    public boolean updatePwd(int id, String pwd) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if (userDao.updatePwd(connection, id, pwd) > 0){
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    @Override
    public int getUserCount(String username, int userRole) {
        Connection connection = null;
        int count = 0;
        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection, username, userRole);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return count;
    }

    @Override
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<User> userList = null;
        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection, queryUserName,queryUserRole,currentPageNo,pageSize);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return userList;
    }


    // 测试getUserList正常
    @Test
    public void test() {
        UserServiceImpl userService = new UserServiceImpl();
        List<User> users = new ArrayList<User>();
        // 从第一页开始

        users = userService.getUserList(null, 0, 1, 5);
        for (User i : users) {
            System.out.println(i.getUserName("userName"));
        }
    }


    // 测试getUserCount正常
    @Test
    public void test2() {
        UserServiceImpl userService = new UserServiceImpl();
        int userCount = userService.getUserCount(null, 3);
        System.out.println(userCount);
    }


}
