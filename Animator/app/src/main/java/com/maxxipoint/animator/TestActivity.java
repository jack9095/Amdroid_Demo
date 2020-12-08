package com.maxxipoint.animator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.maxxipoint.animator.copy.UpVoteLayout;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class TestActivity extends AppCompatActivity {


    private UpVoteLayout viewById;

    long mLastTime = 0;
    long mCurTime = 0;

    public Handler handler = new BaseHandler<>(new BaseHandler.BaseHandlerCallBack() {
        @Override
        public void callBack(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(TestActivity.this, "这是单击事件", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(TestActivity.this, "这是双击事件", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        viewById = findViewById(R.id.likeLayout);

        Function0<Unit> onLikeListener = viewById.getOnLikeListener();

        viewById.setOnLikeListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                Log.e("TestActivity", "这事点赞事件");
                return null;
            }
        });

        viewById.setOnPauseListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                Log.e("TestActivity", "这事暂停事件");
                return null;
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLastTime = mCurTime;
                mCurTime = System.currentTimeMillis();
                if (mCurTime - mLastTime < 300) {//双击事件
                    mCurTime = 0;
                    mLastTime = 0;
                    handler.removeMessages(1);
                    handler.sendEmptyMessage(2);
                } else {//单击事件
                    handler.sendEmptyMessageDelayed(1, 310);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewById.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewById.onDestroy();
    }
}
