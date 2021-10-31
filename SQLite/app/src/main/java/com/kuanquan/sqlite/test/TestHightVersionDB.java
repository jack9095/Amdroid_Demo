package com.kuanquan.sqlite.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
//import android.test.AndroidTestCase;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.kuanquan.sqlite.MySQLiteOpenHelper;
import com.kuanquan.sqlite.bean.Person;
import com.kuanquan.sqlite.dao.ControlDao;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static androidx.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.assertEquals;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-08-23
 * Time: 15:57
 * Description: 单元测试
 * SDK27以上不能使用这个单元测试了，比较奇怪
 * https://blog.csdn.net/xu331700/article/details/113207810
 */
@RunWith(AndroidJUnit4.class)
public class TestHightVersionDB {

    @Test
    public void testCreateDB() throws Exception{
        Context context= InstrumentationRegistry.getInstrumentation().getTargetContext();
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(context);
        helper.getWritableDatabase();
    }

    //添加
    @Test
    public void testAdd() throws Exception{
        ControlDao dao = new ControlDao(getContext());
        dao.add("wangfei", "18072850706");
    }

    //查找
    @Test
    public void testFind() throws Exception{
        ControlDao dao = new ControlDao(getContext());
        boolean result = dao.find("wangfei");
        assertEquals(true, result);
    }

    //修改
    @Test
    public void testModify() throws Exception{
        ControlDao dao = new ControlDao(getContext());
        dao.modify("wangfei", "18072850708");
    }

    //删除
    @Test
    public void testDelete() throws Exception{
        ControlDao dao = new ControlDao(getContext());
        dao.delete("wangfei");
    }

    //查找所有
    @Test
    public void testFindAll() throws Exception{
        ControlDao dao = new ControlDao(getContext());
        List<Person> persons = dao.findAll();
        for(Person p: persons){
            System.out.println(p.toString());
        }
    }

    //数据库的事务
    @Test
    public void testTransaction() throws Exception {
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            //事务操作的完整过程

            //标记数据库事务操作成功
            db.setTransactionSuccessful();
        }finally{
            db.endTransaction();
            db.close();
        }
    }
}
