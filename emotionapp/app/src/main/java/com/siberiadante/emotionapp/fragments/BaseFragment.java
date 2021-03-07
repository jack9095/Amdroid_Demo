package com.siberiadante.emotionapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * Created by SiberiaDante
 * Describe: 基类BaseFragment中的传递参数args可以供子类选择性使用
 * Time: 2017/6/26
 * Email: 994537867@qq.com
 * GitHub: https://github.com/SiberiaDante
 * 博客园： http://www.cnblogs.com/shen-hua/
 */
public class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();
    //传递过来的参数Bundle，供子类使用
    protected Bundle args;

    /**
     * 创建fragment的静态方法，方便传递参数
     *
     * @param args 传递的参数
     * @return
     */
    public static <T extends Fragment> T newInstance(Class clazz, Bundle args) {
        T mFragment = null;
        try {
            mFragment = (T) clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
        mFragment.setArguments(args);
        return mFragment;
    }

    /**
     * 初始创建Fragment对象时调用
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }

}
