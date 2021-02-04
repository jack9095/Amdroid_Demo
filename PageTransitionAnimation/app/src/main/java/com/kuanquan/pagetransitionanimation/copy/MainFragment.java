package com.kuanquan.pagetransitionanimation.copy;

import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kuanquan.pagetransitionanimation.R;
import com.kuanquan.pagetransitionanimation.adapter.MyAdapter;
import com.kuanquan.pagetransitionanimation.elementspage.ShareElementsActivity;
import com.kuanquan.pagetransitionanimation.util.DataUtil;
import com.kuanquan.pagetransitionanimation.viewpager.BaseFragment;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MainFragment  extends BaseFragment {

    public List<String> datas = DataUtil.INSTANCE.getData();
    private MyAdapter myAdapter;
    public LinearLayoutManager linearLayoutManager;
    public Bundle bundle;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(requireActivity());
        myAdapter = new MyAdapter(getActivity(), datas);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter.setOnClickListener((v, position) -> {
            Intent intent = new Intent(getContext(), ShareElementsActivity.class);
            intent.putExtra("url", (Serializable) datas);
            intent.putExtra("index", position);

            // TODO 1. 共享元素动画  必备的步骤
            Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), v, datas.get(position)).toBundle();
            startActivity(intent,options);
        });


        // TODO 2. 共享元素动画  必备的步骤
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().setExitSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    if (bundle != null) {
                        int position = bundle.getInt("index", 0);
                        Log.e("onMapSharedElements", "position = " + position);
                        sharedElements.clear();
                        names.clear();
                        View itemView = linearLayoutManager.findViewByPosition(position);
                        ImageView imageView = itemView.findViewById(R.id.image);
                        Log.e("MainFragment -> ", itemView.getTransitionName() + "");
//                        names.add(itemView.getTransitionName());
                        names.add(null);
                        //注意这里第二个参数，如果防止是的条目的item则动画不自然。放置对应的imageView则完美
                        sharedElements.put(datas.get(position), imageView);
                        bundle = null;
                    }
                }
            });
        }
    }

}
