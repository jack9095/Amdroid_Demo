package com.kuanquan.pagetransitionanimation;

import android.content.Intent;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.TransitionSet;
import android.view.View;
import android.widget.ImageView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kuanquan.pagetransitionanimation.adapter.MyAdapter;
import com.kuanquan.pagetransitionanimation.elementspage.ShareElementsActivity;
import com.kuanquan.pagetransitionanimation.util.DataUtil;
import com.kuanquan.pagetransitionanimation.viewpager.BaseFragment;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

public class MainFragment  extends BaseFragment {

    public List<String> datas = DataUtil.INSTANCE.getData();
    private MyAdapter myAdapter;
//    MainActivity mainActivity;
    public GridLayoutManager gridLayoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
        gridLayoutManager = new GridLayoutManager(requireActivity(), 2);
        myAdapter = new MyAdapter(getActivity(), datas);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        myAdapter.setOnClickListener(new MyAdapter.OnClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getContext(), ShareElementsActivity.class);
                intent.putExtra("url", (Serializable) datas);
                intent.putExtra("index", position);

                // TODO 1. 共享元素动画
                Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), v, datas.get(position)).toBundle();
                startActivity(intent,options);
//                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, v, "sharedView").toBundle());
            }
        });

//        TransitionSet mtransitionset=new TransitionSet();//制定过度动画set
//        mtransitionset.addTransition(new ChangeBounds());//改变表框大小
//        mtransitionset.addTransition(new ChangeImageTransform());//图片移动，还可以是其他的，要什么效果自己添加
//        mtransitionset.setDuration(250);
//
//        getActivity().getWindow().setEnterTransition(mtransitionset);//注意，下面是必须的
//        getActivity().getWindow().setExitTransition(mtransitionset);
//        getActivity().getWindow().setSharedElementEnterTransition(mtransitionset);
//        getActivity().getWindow().setSharedElementExitTransition(mtransitionset);

//        mainActivity = (MainActivity)getActivity();

//        View itemView = gridLayoutManager.findViewByPosition(position);
//        ImageView imageView = itemView.findViewById(R.id.image);
//        mainActivity.setAnimatorFragment("", imageView, datas);
    }
}
