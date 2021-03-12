package com.kuanquan.customlayoutmanager.copy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kuanquan.customlayoutmanager.R;
import com.kuanquan.customlayoutmanager.adapter.StackAdapter;
import com.kuanquan.customlayoutmanager.widget.MyRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class StackActivityCopy extends AppCompatActivity {

    private List<Integer> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack);
        initData();
        initView();
    }

    private void initView() {
        final MyRecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new StackAdapter());
//        PagerSnapHelper snapHelper = new PagerSnapHelper() {
//            // 在 Adapter的 onBindViewHolder 之后执行
//            @Override
//            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
//                // TODO 找到对应的Index
//                Log.e("xiaxl: ", "---findTargetSnapPosition---");
//                int targetPos = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
//                Log.e("xiaxl: ", "targetPos: " + targetPos);
//
//                Toast.makeText(StackActivity.this, "滑到到 " + targetPos + "位置", Toast.LENGTH_SHORT).show();
//
//                return targetPos;
//            }
//
//            // 在 Adapter的 onBindViewHolder 之后执行
//            @Nullable
//            @Override
//            public View findSnapView(RecyclerView.LayoutManager layoutManager) {
//                // TODO 找到对应的View
//                Log.e("xiaxl: ", "---findSnapView---");
//                View view = super.findSnapView(layoutManager);
//
//                return view;
//            }
//        };
//
//        snapHelper.attachToRecyclerView(recyclerView);



//        recyclerView.setAdapter(new MyAdapter(list));
//        CardItemTouchHelperCallback cardCallback = new CardItemTouchHelperCallback(recyclerView.getAdapter(), list);
//        cardCallback.setOnSwipedListener(new OnSwipeListener<Integer>() {
//
//            @Override
//            public void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
//                MyAdapter.MyViewHolder myHolder = (MyAdapter.MyViewHolder) viewHolder;
//                viewHolder.itemView.setAlpha(1 - Math.abs(ratio) * 0.2f);
//                if (direction == CardConfig.SWIPING_LEFT) {
//                    myHolder.dislikeImageView.setAlpha(Math.abs(ratio));
//                } else if (direction == CardConfig.SWIPING_RIGHT) {
//                    myHolder.likeImageView.setAlpha(Math.abs(ratio));
//                } else {
//                    myHolder.dislikeImageView.setAlpha(0f);
//                    myHolder.likeImageView.setAlpha(0f);
//                }
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, Integer o, int direction) {
//                MyAdapter.MyViewHolder myHolder = (MyAdapter.MyViewHolder) viewHolder;
//                viewHolder.itemView.setAlpha(1f);
//                myHolder.dislikeImageView.setAlpha(0f);
//                myHolder.likeImageView.setAlpha(0f);
//                Toast.makeText(StackActivity.this, direction == CardConfig.SWIPED_LEFT ? "swiped left" : "swiped right", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSwipedClear() {
//                Toast.makeText(StackActivity.this, "data clear", Toast.LENGTH_SHORT).show();
//                recyclerView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        initData();
//                        recyclerView.getAdapter().notifyDataSetChanged();
//                    }
//                }, 3000L);
//            }
//
//        });
//        final ItemTouchHelper touchHelper = new ItemTouchHelper(cardCallback);
//        final CardLayoutManager cardLayoutManager = new CardLayoutManager(recyclerView, touchHelper);
//        recyclerView.setLayoutManager(cardLayoutManager);
//        touchHelper.attachToRecyclerView(recyclerView);
    }

    private void initData() {
        list.add(R.drawable.img_avatar_01);
        list.add(R.drawable.img_avatar_02);
        list.add(R.drawable.img_avatar_03);
        list.add(R.drawable.img_avatar_04);
        list.add(R.drawable.img_avatar_05);
        list.add(R.drawable.img_avatar_06);
        list.add(R.drawable.img_avatar_07);
    }

}
