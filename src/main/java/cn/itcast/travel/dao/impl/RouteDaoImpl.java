package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());


    @Override
    public int findTotalCount(int cid , String rname) {
        //String sql = "select count(*) from tab_route where cid = ?";
        String sql = "select count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        List params = new ArrayList();

        if(cid != 0){
            sb.append(" and cid = ? ");
            params.add(cid); //添加对应?的值
        }
        if(rname != null && rname.length()>0 && !"null".equalsIgnoreCase(rname)){
            sb.append(" and rname like ? ");
            params.add("%"+rname+"%");
        }

        sql = sb.toString();
        return template.queryForObject(sql, Integer.class, params.toArray());
    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize , String rname) {
        //String sql = "select * from tab_route where cid = ? limit ?,?";
        String sql = "select * from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        List params = new ArrayList();
        if(cid != 0 ){ //若前台没有传cid，则cid为null字符串，即"null"，并且在RouteServlet.java中已经判断好了，若没有cid，则cid赋值0
            sb.append(" and cid = ?");
            params.add(cid);
        }
        if(rname != null && rname.length() > 0 && !"null".equalsIgnoreCase(rname)){ //在这里判断，如果只查cid，搜索框没有值，即传到后台为"null",则where子句没有rname条件
            sb.append(" and rname like ? ");
            params.add("%"+rname+"%");
        }
        sb.append(" limit ? , ? ");
        sql = sb.toString();

        params.add(start);
        params.add(pageSize);

        List<Route> route = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), params.toArray());
        return route;
    }
}
