package com.kuanquan.roomdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kuanquan.roomdb.dao.EmperorDao;
import com.kuanquan.roomdb.db.MyDatabase;
import com.kuanquan.roomdb.model.Emperor;

import java.util.List;

/**
 * 需要在工作线程中使用数据库，否则耗时操作将会导致Application崩溃
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EmperorDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dao = MyDatabase.getInstance(this).getEmperorDao();

        initViews();
        initViewModel();
    }

    private void initViews() {
        findViewById(R.id.btn_insertEmperor).setOnClickListener(this);
        findViewById(R.id.btn_deleteEmperor).setOnClickListener(this);
        findViewById(R.id.btn_updateEmperor).setOnClickListener(this);
        findViewById(R.id.btn_queryEmperorById).setOnClickListener(this);
        findViewById(R.id.btn_queryEmperorsByAge).setOnClickListener(this);
        findViewById(R.id.btn_queryEmperors).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insertEmperor:
                new Thread(() -> {
                    Emperor caoCao = new Emperor(1, "曹操", 54);
                    Emperor liuBei = new Emperor("刘备", 34);
                    Emperor sunQuan = new Emperor(4,"孙权", 18);
                    dao.insertEmperor(caoCao, liuBei, sunQuan);
                }).start();
                break;
            case R.id.btn_deleteEmperor:
                new Thread(() -> {
//                    Emperor caoCao = dao.queryEmperorById(1);
//                    Emperor liuBei = dao.queryEmperorById(2);
                    Emperor sunQuan = dao.queryEmperorById(4);
//                    dao.deleteEmperor(caoCao, liuBei, sunQuan);
                    dao.deleteEmperor(sunQuan);
                }).start();
                break;
            case R.id.btn_updateEmperor:
                new Thread(() -> {
//                    Emperor caoCao = dao.queryEmperorById(1);
//                    caoCao.age = 34;
//                    Emperor liuBei = dao.queryEmperorById(2);
//                    liuBei.age = 49;
//
//                    dao.updateEmperor(caoCao, liuBei);
                }).start();
                break;
            case R.id.btn_queryEmperorById:
                new Thread(() -> {
                    Emperor sunQuan = dao.queryEmperorById(1);
                }).start();
                break;
            case R.id.btn_queryEmperorsByAge:
                new Thread(() -> {
                    List<Emperor> list = dao.queryEmperorsByAge("54");
                }).start();
                break;
            case R.id.btn_queryEmperors:
                new Thread(() -> {
                    List<Emperor> emperors = dao.queryEmperors();
                }).start();
                break;
        }
    }

    private void initViewModel() {
        EmperorViewModel emperorViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(EmperorViewModel.class);
        emperorViewModel.getListEmperor().observe(this, new Observer<List<Emperor>>() {
            @Override
            public void onChanged(List<Emperor> emperors) {
                for (int i = 0; i < emperors.size(); i++) {
                    Log.e("jetpack", emperors.get(i).toString());
                }
            }
        });
    }
}