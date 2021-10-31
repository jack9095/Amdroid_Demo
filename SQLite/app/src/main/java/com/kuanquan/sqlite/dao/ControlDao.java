package com.kuanquan.sqlite.dao;

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
 * Description: 利用SQL语句实现数据库的增删改查操作的类
 */
public class ControlDao {
    private MySQLiteOpenHelper helper;

    public ControlDao(Context context){
        helper = new MySQLiteOpenHelper(context);
    }

    /**
     * 添加一条记录到数据库
     * @param name 姓名
     * @param phonenum 手机号码
     */
    public void add (String name, String phonenum){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into fly (name,phonenum) values (?,?)",new Object[]{name,phonenum});
        db.close();
    }

    /**
     * 删除一条记录
     * @param name 姓名
     */
    public void delete(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from fly where name =?", new Object[]{name});
        db.close();
    }

    /**
     * 修改记录
     * @param name 姓名
     * @param newnum 新的手机号码
     */
    public void modify(String name, String newnum){
        SQLiteDatabase db = helper.getReadableDatabase();
        db.execSQL("update fly set phonenum =? where name = ?",new Object[]{newnum,name});
        db.close();
        //
    }

    /**
     * 按姓名查找记录是够存在
     * @param name 姓名
     * @return true 存在 false 不存在
     */
    public boolean find(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from fly where name =?", new String[]{name});
        boolean result = cursor.moveToNext();
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 按姓名查找记录是够存在
     *
     * @param name 姓名
     * @return string 电话号码
     */
    public String findPhone(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from fly where name =?", new String[]{name});
        String phone = cursor.getString(cursor.getColumnIndex("phonenum"));
        cursor.close();
        db.close();
        return phone;
    }

    /**
     * 返回全部的数据库信息
     * @return
     */
    public List<Person> findAll(){
        SQLiteDatabase db =  helper.getWritableDatabase();
        List<Person> persons = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from fly", null);
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