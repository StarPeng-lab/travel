package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

public interface RouteService {
    /**
     * 根据类别cid，要搜索的线路名称rname，以及从前台传来的当前页码currentPage和每页显示条数pageSize，进行分页查询
     * @param cid
     * @param currentPage
     * @param pageSize
     * @param rname
     * @return
     */
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname);

    /**
     * 根据线路rid查询一个旅游线路Route对象的详细信息
     * @param rid
     * @return
     */
    public Route findOne(String rid);
}
