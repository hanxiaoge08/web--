package com.cyz.actions;

import com.cyz.bean.Questions;
import com.cyz.dao.AnsDao;
import com.cyz.dao.QuestionsDao;
import com.cyz.dao.StuDao;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.List;

public class Tj extends ActionSupport {
    @Override
    public String execute() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        RequestDispatcher d = request.getRequestDispatcher("b.jsp");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;setchar=UTF-8");// 处理弹出框的中文乱码问题
        
        PrintWriter out = response.getWriter();
        String stuid=request.getParameter("text1");
        String name=request.getParameter("key");
        StuDao studao=new StuDao();
        String sid=studao.delete(stuid, name);
        String id = request.getParameter("q_id");
        if(sid==null)
        {	
        	out.print("<script>alert('请输入正确的学号和姓名！')</script>");
            out.print("<script>window.history.back(-1)</script>");
            out.flush();
            out.close();
            return null;
        }
        String wd="";
        String dx = request.getParameter("dx");
        int snum=0;
        int idx = Integer.parseInt(dx);
        if(idx!=0)
            wd=wd+"#单选";
        for (int i = 0; i < idx; i++) {
            String ddxqid = request.getParameter("ddxqid" + i);
            String dfen = "";
            String[] answers = null;
            String answer = new QuestionsDao().getAnswers(id, ddxqid);
            String content = new QuestionsDao().getOption(ddxqid);
            if (answer != null) {
                answers = answer.split("&");
            }
            String[] contens = content.split("#");
            String ddx = request.getParameter("dx" + i);
            for (int j = 1; j < contens.length; j++) {
                if (j == 1) {
                    if (contens[j].substring(4).equals(ddx)) {
                        int ifen = Integer.parseInt(answers[j]) + 1;
                        dfen = dfen + "&" + ifen;
                    } else {
                        dfen = dfen + "&" + answers[j];
                    }
                } else {
                    if (contens[j].equals(ddx)) {
                        int ifen = Integer.parseInt(answers[j]) + 1;
                        dfen = dfen + "&" + ifen;
                    } else {
                        dfen = dfen + "&" + answers[j];

                    }
                }
            }
            new QuestionsDao().UpdateAnswers(id, ddxqid, dfen);
            String ddx1=ddx.substring(0, 1);
            int num=0;
            if(ddx1.equals("差")) {
            	ddx="3";
            	num=3;
            }else if(ddx1.equals("中")) {
            	ddx="6";
            	num=6;
            }else if(ddx1.equals("良")) {
            	ddx="8";
            	num=8;
            }else if(ddx1.equals("优")) {
            	ddx="9";
            	num=3;
            }
            wd=wd+ddxqid+"."+ddx+" ";
            snum=snum+num;
        }
        if(snum!=0) {
        	wd=wd+"* 总分"+snum;
        }

        String mx = request.getParameter("mx");
        int imx = Integer.parseInt(mx);
        if(imx!=0)
        	wd=wd+"#多选";
        for (int i = 0; i < imx; i++) {
            String dmx = request.getParameter("imx" + i);
            String dxqid = request.getParameter("dxqid" + i);
            String answer = new QuestionsDao().getAnswers(id, dxqid);
            int idmx = Integer.parseInt(dmx);
            String[] dxanswer = null;
            String dfen = "";
            if (answer != null) {
                dxanswer = answer.split("&");
            } else {
                dxanswer = new String[idmx];
            }
            wd=wd+dxqid+".";
            for (int j = 0; j < idmx; j++) {
                String iddmx = request.getParameter("mx" + i + j);
                if (iddmx != null) {
                    String fen = dxanswer[j + 1];
                    int ifen = Integer.parseInt(fen) + 1;
                    dfen = dfen + "&" + ifen;
                    wd=wd+iddmx+" ";
                } else {
                    dfen = dfen + "&"+dxanswer[j + 1];
                }
            }
            new QuestionsDao().UpdateAnswers(id, dxqid, dfen);
        }

        String tk = request.getParameter("tk");
        int itk = Integer.parseInt(tk);
        int iw=0,sum1=0;
        snum = 0;
        if(itk!=0)
        	wd=wd+"#填空";
        for (int i = 0; i < itk; i++) {
            String dtk = request.getParameter("tk" + i);
            char[] ch = dtk.toCharArray();
            String qtkid = request.getParameter("tkqid" + i);
            String answer = new QuestionsDao().getAnswers(id, qtkid);
            answer = answer + "&" + dtk;
            if(dtk=="") {
            	wd=wd+qtkid+"."+dtk+" ";
                new QuestionsDao().UpdateAnswers(id, qtkid, answer);
                continue;
            }
            if(ch[0]>='1'&&ch[0]<='9') {
            	iw=Integer.parseInt(dtk);
                snum=snum+iw;
                sum1++;
            }else {
            	iw=0;
            }
            if(iw<0||iw>100) {
            	out.print("<script>alert('请注意数据范围0-100')</script>");
                out.print("<script>window.history.back(-1)</script>");
                out.flush();
                out.close();
                return null;
            }
            wd=wd+qtkid+"."+dtk+" ";
            new QuestionsDao().UpdateAnswers(id, qtkid, answer);
        }
        if(snum>0) {
        	wd=wd+"* 总分"+snum+"* 平均分"+(snum*1.0/sum1);
        }
        boolean b=AnsDao.insert(sid,id,wd);
        if(b==false)
        {
        	out.print("<script>alert('提交失败请重新提交')</script>");
            out.print("<script>window.history.back(-1)</script>");
            out.flush();
            out.close();
            return null;
        }
        return "success";
    }
}
