package com.kuanquan.handlerthread;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private HandlerThread handlerThread;
    private Handler mainHandler,workHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainHandler = new Handler();

        // 传入参数 = 线程名字，作用 = 标记该线程
        handlerThread = new HandlerThread("handlerThread_A");

        // 启动线程
        handlerThread.start();
        /*
         * 创建工作线程 Handler
         * 作用：关联 HandlerThread 的 Looper 对象、实现消息处理操作 & 与其他线程进行通信
         * 注：消息处理操作（HandlerMessage()）的执行线程 = handlerThread 所创建的工作线程中执行
         */
        workHandler = new Handler(handlerThread.getLooper(), new Handler.Callback() {
            // Handler.Callback 实现的，作用用来拦截消息，返回true，消息就不会传递到 内部类实现的方法中
            @Override
            public boolean handleMessage(@NonNull final Message msg) {
                switch (msg.what) {
                    case 1:
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // 通过主线程的 mainHandler.post 方法进行主线程的 UI 更新操作
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("MainActivity", msg.obj + " 第一次执行 " + Thread.currentThread().getName());
                            }
                        });
                        break;
                    case 2:
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.e("MainActivity", msg.obj + " 第二次执行 " + Thread.currentThread().getName());
                        break;
                }
                return false;
            }
        }) {
            // 内部类实现的
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Log.e("MainActivity", msg.obj + " 内部类实现的 " + Thread.currentThread().getName());
            }
        };

        findViewById(R.id.btn_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = workHandler.obtainMessage();
                message.what = 1;
                message.obj = "A";
                workHandler.sendMessage(message);
            }
        });

        findViewById(R.id.btn_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = Message.obtain();
                message.what = 2;
                message.obj = "B";
                workHandler.sendMessage(message);

                workHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("workHandler.post = ", Thread.currentThread().getName());
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerThread.quit(); // 退出消息循环
        workHandler.removeCallbacksAndMessages(null); // 防止Handler内存泄露 清空消息队列
    }
}
