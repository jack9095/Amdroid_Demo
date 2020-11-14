package com.example.expandtextview.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expandtextview.R;
import com.example.expandtextview.adapter.TestArrayAdapter;

/**
 * @author: njb
 * @date: 2019/10/31 11:22
 * @description: 描述
 */
public class SpinnerActivity extends AppCompatActivity {
    private Spinner spinner;
    private ArrayAdapter<String> mAdapter ;
    private String [] mStringArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);
        initView();
    }

    private void initView() {
        spinner = findViewById(R.id.spinner);
        mStringArray=getResources().getStringArray(R.array.courier_array);
        //使用自定义的ArrayAdapter
        mAdapter = new TestArrayAdapter(this,mStringArray);

        //设置下拉列表风格(这句不些也行)
        //mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mAdapter);
        //监听Item选中事件
        spinner.setOnItemSelectedListener(new ItemSelectedListenerImpl());
    }

    private class ItemSelectedListenerImpl implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
            Log.d("position",String.valueOf(position));
            Toast.makeText(SpinnerActivity.this,"选中了:"+mStringArray[position],Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}

    }
}
