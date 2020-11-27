package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {
    /**
     * 返回总记录数
     * @param cid
     * @return
     */
    public int findTotalCount(int cid);

    /**
     * 返回每一页的数据
     * @param cid
     * @param start
     * @param pageSize
     * @return
     */
    public List<Route> findByPage(int cid , int start , int pageSize);
}
