package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavoriteDao {
    /**
     * 根据线路rid和用户uid判断，此线路是否被用户收藏
     * @param rid
     * @param uid
     * @return
     */
    public Favorite findByRidAndUid(int rid , int uid);

    /**
     * 根据线路rid，查询被收藏过多少次
     * @param rid
     * @return
     */
    public int findCountByRid(int rid);

    public void add(int rid , int uid);
}
