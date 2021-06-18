package com.cyz.actions;

import com.cyz.bean.User;
import com.cyz.dao.LRDao;
import com.cyz.util.MD5Util;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class AddUserAction extends ActionSupport {
    @Override
    public String execute() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;setchar=UTF-8");// 处理弹出框的中文乱码问题
        String name = request.getParameter("username");
        String psw0 = request.getParameter("password");
        RequestDispatcher d = request.getRequestDispatcher("home.jsp");
        String code = request.getParameter("rand");
        String session_code = (String) request.getSession().getAttribute("Code");

        String psw = MD5Util.MD5Encode(psw0, "UTF-8");

        PrintWriter out = response.getWriter();

        User u = new User(name, psw);
        LRDao r = new LRDao();
        u.setUsername(name);
        u.setPassword(psw);
        boolean b = r.insert(u);
        if (session_code.equals(code)) {
            out.print("<script>alert('添加成功！继续添加')</script>");
            out.print("<script>window.location.href='adduser_g.jsp'</script>");
            out.flush();
            out.close();
               /* response.getWriter().write("添加成功，等待<span id = 'one'>3</span>秒后跳转!" +
                    "<script type='text/javaScript' >" +
                    "var span = document.getElementById('one');" +
                    "var i =3;" +
                    "function fun(){" +
                    "i--;" +
                    "if(i>=0){" +
                    "span.innerHTML = i;" +
                    "}" +
                    "}" +
                    "window.setInterval(fun,1000);" +
                    "</script>");*/

//            response.setHeader("refresh", "3;URL=adduser_g.jsp");
        }
        return null;
    }
}


