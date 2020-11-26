package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //完成方法分发
        String uri = request.getRequestURI(); //获得请求路径
        String methodName = uri.substring(uri.lastIndexOf("/")+1); //获得访问的方法名 http://localhost/travel/user/find

        //this: 谁调用this，this就代表谁，例：cn.itcast.travel.web.servlet.UserServlet
        try {
            //获得方法对象
            Method method = this.getClass().getMethod(methodName,HttpServletRequest.class,HttpServletResponse.class);
            //method.setAccessible(true); //暴力反射 ，需要先忽略访问权限修饰符来获取方法: getDeclaredMethod
            //执行方法
            method.invoke(this,request,response);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     *（封装抽取这两个方法）
     * 直接将传入的对象序列化为json，并写回客户端
     * @param obj
     */
    public void writeValue(Object obj, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),obj);
    }

    /**
     * 将传入的对象序列化为json，并返回json数据，（这里没有将json写回客户端）
     * @param obj
     */
    public String writeValueAsString(Object obj, HttpServletResponse response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);

        //将json写回客户端
        //response.setContentType("application/json;charset=utf-8");
        //response.getWriter().write(mapper.writeValueAsString(obj));
    }
}
