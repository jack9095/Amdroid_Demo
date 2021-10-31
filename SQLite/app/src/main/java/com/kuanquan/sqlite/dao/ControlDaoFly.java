package com.kuanquan.sqlite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.kuanquan.sqlite.MySQLiteOpenHelper;
import com.kuanquan.sqlite.bean.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-08-23
 * Time: 15:57
 * Description: 利用系统API函数实现数据库的增删改查等操作的类
 */
public class ControlDaoFly {
    private MySQLiteOpenHelper helper;

    public ControlDaoFly(Context context){
        helper = new MySQLiteOpenHelper(context);
    }
    /**
     * 添加一条记录到数据库
     * @param name 姓名
     * @param phonenum 手机号码
     */
    public long add (String name, String phonenum){//插入成功，返回记录的id，插入失败，返回-1；
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phonenum", phonenum);
        long id = db.insert("fly", null, values);//fly是该数据库中的表名
        db.close();
        return id;
    }

    /**
     * 按姓名查找记录是否存在
     * @param name 姓名
     * @return true 存在 ；false 不存在
     */
    public boolean find(String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("fly", null, "name =?", new String[]{name}, null, null, null);
        boolean result = cursor.moveToNext();
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 修改记录
     * @param name 姓名
     * @param newnum 新的手机号码
     */
    public void modify(String name, String newnum){
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("phonenum", newnum);
        db.update("fly", values, "name = ?", new String[]{newnum});
        db.close();
    }

    /**
     * 删除一条记录
     * @param name 姓名
     */
    public void delete(String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        //db.execSQL("delete from t_test where name =?",new Object[]{name});
        db.delete("fly", "name =?", new String[]{name});
        db.close();
    }

    /**
     * 返回全部的数据库信息
     * @return
     */
    public List<Person> findAll(){
        SQLiteDatabase db =  helper.getWritableDatabase();
        List<Person> persons = new ArrayList<>();
        Cursor cursor = db.query("fly", new String[]{"name","id","phonenum"}, null, null, null, null, null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String phonenum = cursor.getString(cursor.getColumnIndex("phonenum"));
            Person p = new Person(id, name, phonenum);
            persons.add(p);
        }
        cursor.close();
        db.close();
        return persons;
    }
}