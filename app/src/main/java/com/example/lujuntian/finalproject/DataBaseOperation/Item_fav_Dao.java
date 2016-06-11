package com.example.lujuntian.finalproject.DataBaseOperation;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by sky on 16/6/8.
 */

public class Item_fav_Dao implements Item_fav_Service{

    private DBOpenHelper helper = null;

    public Item_fav_Dao(Context context){
        helper = new DBOpenHelper(context);
    }
    @Override
    public boolean addItem(Object[] params) {                   //添加数据
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            //这里面问好表示占位符，所以要需要传入所有的占位符的值,传入值有这个方法中的参数传递
            String sql = "insert into final_list(item_id,item_pic,item_title,item_price,item_sold) values(?,?,?,?,?)";
            database = helper.getWritableDatabase(); //实现对数据库写的操作
            database.execSQL(sql, params);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(database != null) {
                database.close();
            }
        }
        return flag;
    }
    @Override
    public boolean deleteAll(){
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            String sql = "delete from final_list ";
            database = helper.getWritableDatabase();
            database.execSQL(sql);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(database != null) {
                database.close();
            }
        }
        return flag;
    }

    @Override
    public boolean deleteItem(Object[] params) {            //删除数据
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            String sql = "delete from final_list where item_id = ? ";
            database = helper.getWritableDatabase();
            database.execSQL(sql, params);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(database != null) {
                database.close();
            }
        }
        return flag;
    }

    @Override
    public boolean updateItem(Object[] params) {            //更新数据
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            String sql = "update final_list set item_id = ?, item_pic = ?, item_title = ?,item_price where id = ? ";
            database = helper.getWritableDatabase();
            database.execSQL(sql, params);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(database != null) {
                database.close();
            }
        }
        return flag;
    }

    @Override
    public Map<String, String> viewItem(String[] selectionArgs) {       //查询单条数据
        Map<String, String> map = new HashMap<String, String>();
        SQLiteDatabase database = null;
        try {
            String sql = "select * from final_list where item_id = ? ";
            database = helper.getReadableDatabase(); //查询读取数据，查询结果使用Map来存储
            //声明一个游标，这个是行查询的操作，支持原生SQL语句的查询
            Cursor cursor = database.rawQuery(sql, selectionArgs); //ID所在行查询
            int colums = cursor.getColumnCount();//获得数据库的列的个数
            //cursor.moveToNext() 移动到下一条记录
            while(cursor.moveToNext()){
                for(int i = 0; i < colums; i++) {
                    String cols_name = cursor.getColumnName(i); //提取列的名称
                    String cols_value = cursor.getString(cursor.getColumnIndex(cols_name)); //根据列的名称提取列的值
                    //数据库中有写记录是允许有空值的,所以这边需要做一个处理
                    if(cols_value == null) {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }
            }
            if(!cursor.isClosed()){
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(database != null){
                database.close();
            }
        }
        return map;
    }

    @Override
    public List<Map<String, String>> listItemMaps(String[] selectionArgs) {         //查询所有数据
        List<Map<String, String>> list = new ArrayList<Map<String,String>>();
        SQLiteDatabase database = null;
        try {
            String sql = "select * from final_list "; //这个是查询表中所有的内容，所以就不需要传入的这个参数值了
            database = helper.getReadableDatabase();
            Cursor cursor = database.rawQuery(sql, selectionArgs);
            int colums = cursor.getColumnCount();
            while(cursor.moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();
                for(int i = 0; i < colums; i++) {
                    String cols_name = cursor.getColumnName(i);
                    String cols_value = cursor.getString(cursor.getColumnIndex(cols_name));
                    if(cols_name == null) {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }
                list.add(map);

            }
            if(!cursor.isClosed()){
                cursor.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {

            if(database != null){
                database.close();
            }
        }
        return list;
    }
}
