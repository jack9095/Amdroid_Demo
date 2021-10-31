package com.kuanquan.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.kuanquan.sqlite.bean.Person;
import com.kuanquan.sqlite.dao.ControlDao;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("提交", "");
        TextView textView = findViewById(R.id.tv);

        // 创建数据库
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this.getApplicationContext());
        helper.getWritableDatabase();

        testAdd();
//        textView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
                testFind();
//            }
//        }, 200);

        testModify();
        testFind();
        testFindAll();
        testDelete();
        testFindName();
    }

    //添加
    public void testAdd() {
        ControlDao dao = new ControlDao(getApplicationContext());
        dao.add("wangfei", "18072850706");
    }

    //查找
    public void testFindName() {
        ControlDao dao = new ControlDao(getApplicationContext());
      boolean result = dao.find("wangfei");
        Log.e(TAG, "存在否 ->" + result);
    }

    //查找
    public void testFind() {
        try {
            ControlDao dao = new ControlDao(getApplicationContext());
            String result = dao.findPhone("wangfei");
//        boolean result = dao.find("wangfei");
            Log.e(TAG, "phone ->" + result);
        } catch (Exception e) {
            Log.e(TAG, "e ->" + e.getMessage());
        }
    }

    //修改
    public void testModify() {
        ControlDao dao = new ControlDao(getApplicationContext());
        dao.modify("wangfei", "18072850708");
    }

    //删除
    public void testDelete() {
        ControlDao dao = new ControlDao(getApplicationContext());
        dao.delete("wangfei");
    }

    //查找所有
    public void testFindAll() {
        ControlDao dao = new ControlDao(getApplicationContext());
        List<Person> persons = dao.findAll();
        for(Person p: persons){
            Log.e(TAG, "Person ->" + p.toString());
        }
    }

    //数据库的事务
    public void testTransaction() {
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getApplicationContext());
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
