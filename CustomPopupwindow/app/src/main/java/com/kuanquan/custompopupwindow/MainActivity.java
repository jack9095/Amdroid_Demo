package com.kuanquan.custompopupwindow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kuanquan.custompopupwindow.utils.FitPopupUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LayoutInflater mInflater;

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInflater = LayoutInflater.from(this);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
    }


    private void initPopup(View anchorView) {
        FitPopupUtil fitPopupUtil = new FitPopupUtil(this);
        fitPopupUtil.setOnClickListener(new FitPopupUtil.OnCommitClickListener() {
            @Override
            public void onClick(String reason) {
                Toast.makeText(MainActivity.this,reason,Toast.LENGTH_SHORT).show();
            }
        });
        fitPopupUtil.showPopup(anchorView);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                initPopup(button1);
                break;
            case R.id.button2:
                initPopup(button2);
                break;
            case R.id.button3:
                SecondActivity.startActivity(this);
                break;
            case R.id.button4:
                initPopup(button4);
                break;
            case R.id.button5:
                initPopup(button5);
                break;
            case R.id.button6:
                initPopup(button6);
            default:
                break;
        }
    }
}
