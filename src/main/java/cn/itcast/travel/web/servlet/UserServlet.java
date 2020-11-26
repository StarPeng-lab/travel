package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {

    //声明UserService的业务对象
    private UserService service = new UserServiceImpl();
    /**
     * 注册
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //校验验证码
        checkCode(request,response);

        //校验注册信息
        //1、获得前端数据
        Map<String, String[]> parameterMap = request.getParameterMap();
        //2、封装在对象中，用BeanUtils将parameterMap的【数据】封装到user的【属性】中
        User user = new User();
        try {
            BeanUtils.populate(user,parameterMap);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        //3、调用service方法，到后台注册用户
        //UserService service = new UserServiceImpl();
        boolean flag = service.regist(user);
        ResultInfo info = new ResultInfo();
        //4、响应结果
        info.setFlag(flag);
        if(!flag){
            info.setErrorMsg("用户名已存在，注册失败！");
        }
        //5、将info对象序列化为json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        //6、将json数据写回客户端
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);

        //demo:（由于邮箱有安全隐患，因此不走邮箱工具类，直接用session存储激活码，点击regist_html的链接即可激活）
        if(flag){
            request.getSession().setAttribute("code",user.getCode());
        }
    }

    /**
     * 激活用户
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void activeUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1、获得激活码
        //String code = request.getParameter("code"); //这里的code是UserServiceImpl类的regist方法中设置的邮件内的路径传来的code
        String code = (String) request.getSession().getAttribute("code"); //这里的code是RegistServlet中存储的session的键

        if(code != null){
            //2、调用service完成激活
            //UserService service = new UserServiceImpl();
            boolean flag = service.active(code);
            //3、判断激活状态
            String msg = null ;
            if(flag){
                //激活成功
                StringBuffer uri = request.getRequestURL(); //http://localhost/travel/user/activeUser
                String path = uri.toString();
                path = path.substring(0,path.lastIndexOf("/")); //截掉方法名
                path = path.substring(0,path.lastIndexOf("/")); //截掉模块名 http://localhost/travel
                msg = "激活成功，请<a href='"+path+"/login.html'>登录</a>";
            }else{
                //激活失败
                msg = "激活失败，请联系管理员！"; //原因一般是用户网络状态不佳，或者后台修改激活码
            }

            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }

    /**
     * 登录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //校验验证码
        checkCode(request,response);

        //1、获得用户名和密码
        Map<String, String[]> map = request.getParameterMap();
        //2、封装user对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        //3、调用serice层方法验证
        //UserService service = new UserServiceImpl();
        User u = service.login(user);
        //4、判断用户名或密码
        ResultInfo info = new ResultInfo();
        if(u == null){
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误！");
        }
        //5、判断是否激活
        if(u != null && !"Y".equals(u.getStatus())){
            info.setFlag(false);
            info.setErrorMsg("用户邮箱未激活！");
        }
        //6、判断登录成功
        if(u != null && "Y".equals(u.getStatus())){
            info.setFlag(true);
            request.getSession().setAttribute("user",u); //在session中记录user，即此用户登录标志
        }

        //7、响应ajax数据
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),info);
    }

    /**
     * 获得登录对象
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从session的user键中获得登录用户对象
        Object user = request.getSession().getAttribute("user");
        //将user写回客户端
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),user);
    }

    /**
     * 用户退出
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1、销毁session
        request.getSession().invalidate();
        //2、重定向到登录页面，全路径名
        response.sendRedirect(request.getContextPath()+"/login.html");
    }

    /**
     * 校验验证码
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void checkCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1、获得前端用户输入的验证码
        String code = request.getParameter("check");
        //2、获得session中存储的验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER"); //保证验证码只使用一次
        //3、校验
        if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(code)){
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误！请刷新验证码");

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return ;
        }
    }
}
