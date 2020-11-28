package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.service.FavoriteService;

public class FavoriteServiceImpl implements FavoriteService {

    FavoriteDao dao = new FavoriteDaoImpl();

    @Override
    public boolean isFavorite(String rid, int uid) {
        int rid_i = 0;
        if( rid != null && !"null".equalsIgnoreCase(rid)){
            rid_i = Integer.parseInt(rid);
        }
        Favorite favorite = dao.findByRidAndUid(rid_i, uid);
        return favorite!=null; //不为空，true，被收藏；为空，false，未被收藏
    }

    @Override
    public void add(String rid, int uid) {
        dao.add(Integer.parseInt(rid),uid);
    }
}
