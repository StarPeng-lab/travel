package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {
    /**
     * 返回总记录数
     * @param cid
     * @return
     */
    public int findTotalCount(int cid , String rname);

    /**
     * 返回每一页的数据
     * @param cid
     * @param start
     * @param pageSize
     * @return
     */
    public List<Route> findByPage(int cid , int start , int pageSize , String rname);

    /**
     * 根据线路rid查询route对象，即旅游线路的详细信息
     * @param rid
     * @return
     */
    public Route findOne(int rid);
}
