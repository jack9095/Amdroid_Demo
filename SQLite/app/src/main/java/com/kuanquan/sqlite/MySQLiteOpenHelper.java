package com.kuanquan.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-08-23
 * Time: 15:57
 * Description: 自定义SQLite数据库打开帮助类
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper{
    /**
     * 数据库构造方法 用来定义数据库的名称 数据库的查询结果记录集 数据库的版本
     * @param context
     */
    public MySQLiteOpenHelper(Context context) {
        super(context, "fly_fly.db", null, 1);

    }

    /**
     * 创建数据库，程序第一次运行时调用
     * @param db 创建的数据库
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表结构     fly创建的表名   id，name，phonenum都是表中的字段，代表这个表有三列
        db.execSQL("create table fly (id integer primary key autoincrement , name varchar(20) , phonenum varchar(11)) ");
    }

    /**
     * 当数据库的版本号发生变化（版本号增加）时调用。
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("alter table fly add group varchar(20)");
    }
}
