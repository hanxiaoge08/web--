package com.cyz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


import com.cyz.bean.User;
import com.cyz.bean.User_g;
import com.cyz.util.BaseConnection;

public class StuDao {
    
    public String delete(String sid,String name) {
    	String id = null;
        String sql = "select * from stu where stuid=? and name=?";
        try {
            Connection conn = BaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,sid);
            ps.setString(2, name);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            	id = rs.getString("id");
            return id;
        } catch (Exception e) {
            e.printStackTrace();// TODO: handle exception

        }
        return null;
    }


}
