package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.RouteService;

public class RouteServiceImpl implements RouteService {

    private RouteDao dao = new RouteDaoImpl();

    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize) {
        //封装PageBean
        PageBean<Route> pb = new PageBean<Route>();
        //设置当前页码
        pb.setCurrentPage(currentPage);
        //设置每页显示的记录数
        pb.setPageSize(pageSize);
        //设置总记录数
        int totalCount = dao.findTotalCount(cid);
        pb.setTotalCount(totalCount);
        //设置总页数 = 总记录数/每页显示记录数
        int totalPage = totalCount%pageSize==0 ? totalCount/pageSize : totalCount/pageSize+1;
        pb.setTotalPage(totalPage);
        //设置当前页显示的数据集合
        int start = (currentPage-1)*pageSize; //0-4,5-9,10-14,15-19,...
        pb.setList(dao.findByPage(cid,start,pageSize));

        return pb;
    }
}
