package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private RouteService service = new RouteServiceImpl();

    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1、接收参数
        String currentPageStr = request.getParameter("currentPage"); //当前页码
        String pageSizeStr = request.getParameter("pageSize"); //每页显示条数
        String cidStr = request.getParameter("cid"); //类别的cid

        //2、处理参数，转换为数据类型
        int currentPage = 0;
        if(currentPageStr != null || currentPageStr.length() > 0){
            currentPage = Integer.parseInt(currentPageStr);
        }else{
            currentPage = 1 ; //如果前台没有传递参数，则默认当前页码为1
        }

        int pageSize = 0;
        if(pageSizeStr != null || pageSizeStr.length() > 0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else{
            pageSize = 5 ; //如果前台没有传递参数，则默认每页显示条数为5
        }

        int cid = 0;
        if(cidStr != null || cidStr.length() > 0){
            cid = Integer.parseInt(cidStr);
        }

        //3、调用service层方法查询PageBean对象
        PageBean<Route> pb = service.pageQuery(cid,currentPage,pageSize);

        //4、将PageBean对象序列化为json，返回客户端
        writeValue(pb,response); //调用BaseServlet封装的方法


    }

}
