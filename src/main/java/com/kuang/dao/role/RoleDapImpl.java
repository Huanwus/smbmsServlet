package com.kuang.dao.role;

import com.kuang.dao.BaseDao;
import com.kuang.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoleDapImpl implements RoleDao{
    // 获取角色列表
    @Override
    public List<Role> getRoleList(Connection connection) throws Exception {
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        ArrayList<Role> roleList = new ArrayList<Role>();
        if (connection!=null){
            String sql = "select * from smbms_role";
            Object[] params = {};
            resultSet = BaseDao.execute(connection, pstm, resultSet, sql, params);
            while (resultSet.next()){
                Role _role = new Role();
                _role.setId(resultSet.getInt("id"));
                _role.setRoleCode(resultSet.getString("roleCode"));
                _role.setRoleName(resultSet.getString("roleName"));
                roleList.add(_role);
            }
            BaseDao.closeResource(null, pstm, resultSet);
        }
        return roleList;
    }
}
