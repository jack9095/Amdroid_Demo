package com.example.expandtextview.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expandtextview.R;
import com.example.expandtextview.bean.WeatherEvent;
import com.example.expandtextview.util.RxBus;
import com.zzhoujay.richtext.RichText;

/**
 * @作者: njb
 * @时间: 2019/8/28 18:24
 * @描述:
 */
public class AddCityActivity extends AppCompatActivity {
    private TextView textView;
    private String text = "\n" +
            "       <p><strong>发票结算协议：<\\/strong><\\/p><p>1、提交发票结算的资料后，供应商收益只能进行发票结算，无法使用个人用户结算；<\\/p><p>2、结算金额默认\n" +
            " 一次结算全部可结算金额；<\\/p><p>3、发票结算方式为全额结算,且结算金额最低要求大于1000个钻石；<\\/p><p>4、结算申请时间为每月2日到4日。<\\/p><p>5、申请结算后,会在15个工作日内进行打款,如银\n" +
            " 行卡信息错误未到账,请联系客服。<\\/p><p>6、若有疑问请致电客服热线: <span style=\\\"color: #D81B60;\\\"><strong>400-608-6868<\\/strong><\\\n" +
            " /span>。<\\/p><p>7、创业天下平台拥有最终解释权。<\\/p><p><br\\/><\\/p>\n";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_send);
        initView();
    }

    private void initView() {
        textView = findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxBus.getInstance().post(new WeatherEvent("021","西安市","30℃"));
                finish();
            }
        });
        if(text.contains("color")){
            String[] str = text.split("color:rbg");
            for(String s:str){
                Log.d("s",s);
            }
           // textView.setTextColor(s);
        }

        RichText.fromHtml(text).into(textView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RichText.clear(this);
    }
}
