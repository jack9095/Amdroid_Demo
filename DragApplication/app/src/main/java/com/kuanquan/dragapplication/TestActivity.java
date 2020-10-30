package com.kuanquan.dragapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.kuanquan.pickturelib.Pickture;
import com.kuanquan.pickturelib.interf.OperateListenerAdapter;
import com.kuanquan.pickturelib.widget.PickRecyclerView;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    private final String TAG = "PICKTURE";

    private ArrayList<String> selectedList = new ArrayList<>();

    private PickRecyclerView mPickRecyclerView;

    private Context mContext = TestActivity.this;

    private final int COLUMN = 4, MAX = 18;
    private Pickture mPickture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mPickRecyclerView = (PickRecyclerView) findViewById(R.id.__prv);

        findViewById(R.id.__get_photo_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启用图片选择功能
                mPickture.selected(selectedList).create();
                //如果只是使用读取照片功能，可使用下面这个快捷通道，不需要再调用showOn方法去同步参数了
//                Pickture.with(MainActivity.this).column(COLUMN).max(MAX).selected(selectedList).create();
            }
        });

        mPickture = Pickture.with(TestActivity.this).column(COLUMN).max(MAX).hasCamera(true).selected(selectedList);

        //当需要同步展示到 PickRecyclerView 需要同步基础参数给你的 mPickRecyclerView ，这个方法就是用于同步的
        mPickture.showOn(mPickRecyclerView);

        mPickRecyclerView.setOnOperateListener(new OperateListenerAdapter() {

            @Override
            public void onClickAdd() {
                //点击添加按钮
                mPickture.selected(selectedList).create();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            selectedList = data.getStringArrayListExtra(Pickture.PARAM_PICKRESULT);

            mPickRecyclerView.bind(selectedList);
        }
    }
}
