package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {

    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        //封装PageBean
        PageBean<Route> pb = new PageBean<Route>();
        //设置当前页码
        pb.setCurrentPage(currentPage);
        //设置每页显示的记录数
        pb.setPageSize(pageSize);
        //设置总记录数
        int totalCount = routeDao.findTotalCount(cid,rname);
        pb.setTotalCount(totalCount);
        //设置总页数 = 总记录数/每页显示记录数
        int totalPage = totalCount%pageSize==0 ? totalCount/pageSize : totalCount/pageSize+1;
        pb.setTotalPage(totalPage);
        //设置当前页显示的数据集合
        int start = (currentPage-1)*pageSize; //0-4,5-9,10-14,15-19,...
        pb.setList(routeDao.findByPage(cid,start,pageSize,rname));

        return pb;
    }

    @Override
    public Route findOne(String rid) {
        //1、根据rid去tab_route表中查询路线详细信息，tab_route表，返回route对象
        Route route = routeDao.findOne(Integer.parseInt(rid));

        //2、根据rid查询图片信息：tab_route_img表，返回集合
        List<RouteImg> routeImgList = routeImgDao.findByRid(route.getRid());
        //2.2、将集合设置到route对象
        route.setRouteImgList(routeImgList);

        //3、根据route对象的商家sid，查询商家信息，tab_seller表，返回商家对象
        Seller seller = sellerDao.findBySid(route.getSid());
        //3.2、将商家对象设置到route对象
        route.setSeller(seller);

        //4、查询路线收藏次数
        int countByRid = favoriteDao.findCountByRid(Integer.parseInt(rid));
        //4.2、将收藏次数设置到route对象
        route.setCount(countByRid);

        return route;
    }
}
