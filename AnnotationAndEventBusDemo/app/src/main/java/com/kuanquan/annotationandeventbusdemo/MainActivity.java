package com.kuanquan.annotationandeventbusdemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.kuanquan.annotationandeventbusdemo.event.InjectView;
import com.kuanquan.annotationandeventbusdemo.event.OnClick;
import com.kuanquan.annotationandeventbusdemo.event.ViewUtil;

/**
 * 1.首先就是获取activity的所有成员方法getDeclaredMethods
 * 2.找到有onClick注解的方法，拿到value就是注解点击事件button的id
 * 3.获取onClick注解的注解EventType的参数，从中可以拿到设定点击事件方法setOnClickListener + 点击事件的监听接口OnClickListener+点击事件的回调方法onClick
 * 4.在点击事件发生的时候Android系统会触发onClick事件，我们需要将事件的处理回调到注解的方法InvokeBtnClick，也就是代理的思想
 * 5.通过动态代理Proxy.newProxyInstance实例化一个实现OnClickListener接口的代理，代理会在onClick事件发生的时候回调InvocationHandler进行处理
 * 6.RealSubject就是activity，因此我们传入ProxyHandler实例化一个InvocationHandler，用来将onClick事件映射到activity中我们注解的方法InvokeBtnClick
 * 7.通过反射实例化Button，findViewByIdMethod.invoke
 * 8.通过Button.setOnClickListener(OnClickListener)进行设定点击事件监听。
 * <p>
 * 链接：https://www.jianshu.com/p/fad15887a05e
 */
public class MainActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @InjectView(R.id.bind_view_btn)
    public Button mBindView;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ViewUtil.inJectView(this);
        ViewUtil.injectEvent(this);


        new Handler().post(new Runnable() {
            @Override
            public void run() {

            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 500);


        new Thread(){
            @Override
            public void run() {
                super.run();
                Message message = handler.obtainMessage();
                handler.sendMessage(message);
            }
        }.start();

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.bind_view_btn, R.id.bind_click_btn})
    public void invokeOnClick(View view) {
        switch (view.getId()) {
            case R.id.bind_view_btn:
                Log.e("bind_view_btn", "点击了 bind_view_btn");
                break;
            case R.id.bind_click_btn:
                Log.e("bind_click_btn", "点击了 bind_click_btn");
                break;
        }
    }

}
