package com.kuanquan.draggridview;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.kuanquan.draggridview.adapter.MainAdapter;
import com.kuanquan.draggridview.drag.*;
import com.kuanquan.draggridview.entity.MenuEntity;
import com.orhanobut.logger.Logger;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    private DragGridViewMain dragGridView;
    private MainAdapter adapterSelect;
    private DragForScrollView sv_index;
    private AppContext appContext;
    private RelativeLayout deleteView;
    private boolean isLongClick;
    private static List<MenuEntity> indexSelect = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appContext = (AppContext) getApplication();
        dragGridView = (DragGridViewMain) findViewById(R.id.gridview);
        sv_index= (DragForScrollView) findViewById(R.id.sv_index);
        deleteView= (RelativeLayout) findViewById(R.id.delete_area);

        initView();
    }

    private void initView() {

        //获取设置保存到本地的菜单
        List<MenuEntity> indexDataList = (List<MenuEntity>) appContext.readObject(AppConfig.KEY_USER);
        if (indexDataList != null) {
            indexSelect.clear();
            indexSelect.addAll(indexDataList);
        }
        adapterSelect = new MainAdapter(this, appContext, indexSelect);
        dragGridView.setAdapter(adapterSelect);

        dragGridView.setDragCallback(new DragCallback() {
            @Override
            public void startDrag(int position) {
                Logger.e("start drag at ", ""+ position);
                sv_index.startDrag(position);
                deleteView.setVisibility(View.VISIBLE);
                deleteView.setBackgroundColor(0x0dffffff);
            }
            @Override
            public void endDrag(int position) {
                Logger.e("end drag at " ,""+ position);
                adapterSelect.setDatas(indexSelect);
                adapterSelect.notifyDataSetChanged();
                sv_index.endDrag(position);
                deleteView.setVisibility(View.INVISIBLE);
                deleteView.setBackgroundColor(0x0dffffff);
            }

            @Override
            public void isDelete(int position, boolean isUp) {
                if (isUp) {
                    Logger.e("删除了 " + position);
                    indexSelect.remove(position);
                }

                deleteView.setBackgroundColor(0x19ffffff);

            }
        });
        dragGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("setOnItemClickListener",adapterSelect.getEditStatue()+" position = " + position);
                if(!adapterSelect.getEditStatue()){
                    //dragGridView.clicked(position);
                    MenuEntity cateModel = indexSelect.get(position);
                }
//                adapterSelect.endEdit();
                isLongClick = false;
            }
        });
        dragGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isLongClick) {
                    isLongClick = true;
//                    adapterSelect.setEdit();
                }
                dragGridView.startDrag(position,deleteView);
                return false;
            }
        });
    }
}