package com.siberiadante.emotionapp;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.siberiadante.emotionapp.fragments.EmotionMainFragment;
import com.siberiadante.emotionapp.widget.EmotionKeyboard;

/**
 * Created by SiberiaDante
 * Describe: 表情加载类,可自己添加多种表情，分别建立不同的map存放和不同的标志符即可
 * Time: 2017/6/26
 * Email: 994537867@qq.com
 * GitHub: https://github.com/SiberiaDante
 * 博客园： http://www.cnblogs.com/shen-hua/
 */

public class EditEmotionActivity extends AppCompatActivity {
    public static final String TAG = EditEmotionActivity.class.getSimpleName();
    private TextView mTvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_edit_emotion);
        initView();
        initData();
    }

    private void initView() {
        mTvContent = (TextView) findViewById(R.id.tv_input_content);
    }

    private void initData() {
        initEmotionMainFragment();
    }

    EmotionMainFragment emotionMainFragment;

    private void initEmotionMainFragment() {
        emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment.class, null);
        emotionMainFragment.bindToContentView(mTvContent);//绑定当前页面控件
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_emotion_view_main, emotionMainFragment);
//        transaction.addToBackStack(null);//fragment添加至回退栈中
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        /**
         * 按下返回键，如果表情显示，则隐藏，没有显示则回退页面
         */
        if (!emotionMainFragment.isInterceptBackPress()) {
            super.onBackPressed();
        }
    }
}
