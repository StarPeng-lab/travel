package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

public interface SellerDao {
    /**
     * 根据商家id查找，返回seller对象
     * @param sid
     * @return
     */
    public Seller findBySid(int sid);
}
