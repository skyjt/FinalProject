package com.example.lujuntian.finalproject.DataBaseOperation;

import java.util.List;
import java.util.Map;

/**
 * Created by sky on 16/6/8.
 */

public interface Item_fav_Service {
    public boolean addItem(Object[] params);

    public boolean deleteItem(Object[] params);
    public boolean deleteAll();
    public boolean updateItem(Object[] params);

    //使用 Map<String, String> 做一个封装，比如说查询数据库的时候返回的单条记录
    public Map<String, String> viewItem(String[] selectionArgs);

    //使用 List<Map<String, String>> 做一个封装，比如说查询数据库的时候返回的多条记录
    public List<Map<String, String>> listItemMaps(String[] selectionArgs);
}
