package com.kuang.service.Role;

import com.kuang.dao.BaseDao;
import com.kuang.dao.role.RoleDao;
import com.kuang.pojo.Role;
import com.kuang.dao.role.RoleDapImpl;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class RoleServiceImpl implements RoleService {

    // 引入Dao
    private RoleDao roleDao;
    public RoleServiceImpl(){
        roleDao =new RoleDapImpl();
    }

    @Override
    public List<Role> getRoleList() {
        Connection connection = null;
        List<Role> roleList = null;
        try {
            connection = BaseDao.getConnection();
            roleList = roleDao.getRoleList(connection);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return roleList;
    }

    // 测试getRoleList正常
    @Test
    public void test() {
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roles = new ArrayList<Role>();
        roles = getRoleList();
        for (Role role : roles) {
            System.out.println(role.getRoleName());
        }
    }
}
