package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {

    private UserDao dao = new UserDaoImpl();

    @Override
    public boolean regist(User user) {
        //根据用户名查询后台是否有此用户对象
        User u = dao.findByUsername(user.getUsername());
        if(u!=null){
            //用户名已存在，注册失败
            return false;
        }

        //用户名不存在，保存此注册用户的信息
        user.setCode(UuidUtil.getUuid());//设置激活码，唯一字符串
        user.setStatus("N");//设置激活码
        dao.save(user);

        //激活邮件发送
        String content = "<a href='http://localhost/travel/activeUserServlet?code="+user.getCode()+"'>点击激活邮箱</a>";
        //MailUtils.sendMail(user.getEmail(),content,"激活邮件");


        return true;
    }

    @Override
    public boolean active(String code) {
        //1、根据激活码查询用户
        User user = dao.findByCode(code);
        if(user != null){
            //2、用户存在，则调用dao的修改激活状态方法
            dao.updateStatus(user);
            return true;
        }
        return false;
    }

    @Override
    public User login(User user) {
       return dao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
}
