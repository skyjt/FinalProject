package com.example.lujuntian.finalproject.DataBaseOperation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sky on 16/6/8.
 */

public class DBOpenHelper extends SQLiteOpenHelper{

    private static String name = "mydb.db"; //表示数据库的名称
    //private static int version = 1; //表示数据库的版本号
    private static int version = 1; //更新数据库的版本号，此时会执行 onUpgrade()方法

    public DBOpenHelper(Context context) {
        super(context, name, null, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table final_list(id integer primary key autoincrement,item_id varchar(64),item_pic varchar(255),item_title varchar(100),item_price varchar(20),item_sold varchar(20))";
        db.execSQL(sql); //完成数据库的创建
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "create table final_list(id integer primary key autoincrement,item_id varchar(64),item_pic varchar(255),item_title varchar(100),item_price varchar(20),item_sold varchar(20))";
        //String sql = "alter table test add test varchar(8)";
        db.execSQL(sql);
    }
}
