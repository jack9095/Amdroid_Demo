//package com.kuanquan.sqlite.test;
//
//import android.database.sqlite.SQLiteDatabase;
//import android.test.AndroidTestCase;
//
//import com.kuanquan.sqlite.MySQLiteOpenHelper;
//import com.kuanquan.sqlite.bean.Person;
//import com.kuanquan.sqlite.dao.ControlDao;
//
//import java.util.List;
//
///**
// * Created by Android Studio
// * author: fei.wang
// * Date: 2016-08-23
// * Time: 15:57
// * Description: 单元测试
// * SDK27以上不能使用这个单元测试了，比较奇怪
// */
//public class TestDB extends AndroidTestCase {
//    public void testCreateDB() throws Exception{
//        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getContext());
//        helper.getWritableDatabase();
//    }
//
//    //添加
//    public void testAdd() throws Exception{
//        ControlDao dao = new ControlDao(getContext());
//        dao.add("wangfei", "18072850706");
//    }
//
//    //查找
//    public void testFind() throws Exception{
//        ControlDao dao = new ControlDao(getContext());
//        boolean result = dao.find("wangfei");
//        assertEquals(true, result);
//    }
//
//    //修改
//    public void testModify() throws Exception{
//        ControlDao dao = new ControlDao(getContext());
//        dao.modify("wangfei", "18072850708");
//    }
//
//    //删除
//    public void testDelete() throws Exception{
//        ControlDao dao = new ControlDao(getContext());
//        dao.delete("wangfei");
//    }
//
//    //查找所有
//    public void testFindAll() throws Exception{
//        ControlDao dao = new ControlDao(getContext());
//        List<Person> persons = dao.findAll();
//        for(Person p: persons){
//            System.out.println(p.toString());
//        }
//    }
//
//    //数据库的事务
//    public void testTransaction() throws Exception {
//        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getContext());
//        SQLiteDatabase db = helper.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            //事务操作的完整过程
//
//            //标记数据库事务操作成功
//            db.setTransactionSuccessful();
//        }finally{
//            db.endTransaction();
//            db.close();
//        }
//    }
//}
