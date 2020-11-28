package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

public interface RouteImgDao {
    /**
     * 根据route对象的rid，查询图片，并存入List集合，即存入route对象的routeImgList字段
     * @param rid
     * @return
     */
    public List<RouteImg> findByRid(int rid);
}
