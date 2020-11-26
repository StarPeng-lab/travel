package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public User findByUsername(String username) {
        //返回一条记录，因此用queryForObject方法，把username填入?，执行sql，得到的数据封装进BeanPropertyRowMapper，返回user对象
        //BeanPropertyRowMapper：它可自动将一行数据映射到指定类的实例中，它首先将这个类实例化，然后通过名称匹配的方式，映射到属性中去

        //如果这里不加 try..catch，查询后封装成 user 对象，如果没有找到这个user对象，则会报下述异常，而不是返回null
        /*org.springframework.dao.EmptyResultDataAccessException:Incorrect result size:excepted 1,actual 0*/
        // 期望查询到1个对象，真正查询到的是0个对象，让它不报这个异常，则加 try..catch
        try {
            String sql = "select * from tab_user where username = ?";
            User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
            return user;
        }catch(DataAccessException e){
            e.printStackTrace();
            return null ;
        }
    }

    @Override
    public void save(User user) {
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(?,?,?,?,?,?,?,?,?)";
        template.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode());
    }

    @Override
    public User findByCode(String code) {
        User user = null ;
        try {
            String sql = "select * from tab_user where code = ?";
            user = template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),code);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void updateStatus(User user) {
        String sql = "update tab_user set status = 'Y' where uid = ?";
        template.update(sql,user.getUid());
    }

    public User findByUsernameAndPassword(String username,String password){
        User user = null ;
        try {
            String sql = "select * from tab_user where username = ? and password = ?";
            user = template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),username,password);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return user;
    }
}
