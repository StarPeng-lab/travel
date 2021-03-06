package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {

    CategoryDao dao = new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {
        //1、查询redis
        Jedis jedis= JedisUtil.getJedis(); //用JedisUtils工具类获取jedis客户端
        //Set<String> zset = jedis.zrange("category", 0, -1); //查询sortedset的值cname，按cid排序
        Set<Tuple> zset = jedis.zrangeWithScores("category", 0, -1);//查询sortedset中的分数cid和值cname，按cid排序(header.html页面中需要携带cid值进行类别跳转: http://localhost/travel/route_list.html?cid=1)

        //2、判断查询集合是否为空
        List<Category> list = null ;
        if(zset == null || zset.size() == 0){
            //为空，第一次访问，从数据库取数：调用service查询所有
            list = dao.findAll();
            //将集合数据存储到redis中 category键的值中
            for(int i = 0 ; i < list.size() ; i++){
                jedis.zadd("category",list.get(i).getCid(),list.get(i).getCname());
            }
        }else{
            //不为空，不是第一次访问，将zset的数据存入list
            list = new ArrayList<Category>();
            /*for(String name : zset){
                Category category = new Category();
                category.setCname(name);
                list.add(category);
            }*/
            for(Tuple tuple : zset){
                Category category = new Category();
                category.setCname(tuple.getElement());
                category.setCid((int)tuple.getScore());
                list.add(category);
            }
        }

        return list;

    }
}
