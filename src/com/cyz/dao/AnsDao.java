package com.cyz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.cyz.bean.User;
import com.cyz.util.BaseConnection;

public class AnsDao {
	
	public static boolean insert(String stuid,String w_id,String conte) {
        boolean b = false;
        try {
            Connection conn = BaseConnection.getConnection();
            PreparedStatement ps = null;
            String sql = "insert into answer (stuid,w_id,conte) " +
                    "values (?,?,?)";//占位符

            ps = conn.prepareStatement(sql);//将写好的数据传递到数据库
            ps.setString(1, stuid);
            ps.setString(2,w_id);
            ps.setString(3,conte);
            
            int a = ps.executeUpdate();//这个方法用于改变数据库数据
            if (a > 0) {
                b = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

}
