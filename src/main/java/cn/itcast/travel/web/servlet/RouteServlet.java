package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1、接收参数
        String currentPageStr = request.getParameter("currentPage"); //当前页码
        String pageSizeStr = request.getParameter("pageSize"); //每页显示条数
        String cidStr = request.getParameter("cid"); //类别的cid

        String rname = request.getParameter("rname"); //接受rname线路名称
        rname = new String(rname.getBytes("iso-8859-1"), "utf-8"); //tomcat8就会自动处理乱码问题，这里用的是tomcat7，因此手动更改编码


        //2、处理参数，转换为数据类型
        int currentPage = 0;
        if(currentPageStr != null && currentPageStr.length() > 0){
            currentPage = Integer.parseInt(currentPageStr);
        }else{
            currentPage = 1 ; //如果前台没有传递参数，则默认当前页码为1
        }

        int pageSize = 0;
        if(pageSizeStr != null && pageSizeStr.length() > 0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else{
            pageSize = 5 ; //如果前台没有传递参数，则默认每页显示条数为5
        }

        int cid = 0; //当没有点击导航栏直接搜索时，会出现?cid=null&rname=西安，cid传入null字符串，导致出现空指针，因为"null"!=null，因此光判断cidStr!=null，无法过滤掉
        if(cidStr != null && cidStr.length() > 0 && !"null".equalsIgnoreCase(cidStr)){
            cid = Integer.parseInt(cidStr);
        }

        //3、调用service层方法查询PageBean对象
        PageBean<Route> pb = routeService.pageQuery(cid,currentPage,pageSize,rname);

        //4、将PageBean对象序列化为json，返回客户端
        writeValue(pb,response); //调用BaseServlet封装的方法


    }

    /**
     * 根据rid(线路id)查询一个旅游线路的详细信息
     * @param request
     * @param response
     */
    public void findOne(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException{
        //1、接收rid
        String rid = request.getParameter("rid");
        //2、调用service层方法查询，并返回route对象
        Route route = routeService.findOne(rid);
        //3、将route对象序列化为json，返回给客户端
        writeValue(route,response);
    }

    /**
     * 判断此路线是否被用户收藏
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException{
        //1、接收参数：路线rid
        String rid = request.getParameter("rid");
        //1.2、根据session中的user键，判断用户是否登录，并获取用户uid
        int uid = 0 ;
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        if(user != null){ // 用户已登录
            uid = user.getUid();
        }

        //2、调用service层：FavoriteService.java接口的实现类，查询是否收藏
        boolean flag = favoriteService.isFavorite(rid,uid);

        //3、写回客户端
        writeValue(flag,response);
    }

}
