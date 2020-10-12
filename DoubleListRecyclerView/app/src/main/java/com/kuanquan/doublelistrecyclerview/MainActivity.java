package com.kuanquan.doublelistrecyclerview;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.kuanquan.doublelistrecyclerview.adapte.ClassAdapter;
import com.kuanquan.doublelistrecyclerview.adapte.StudentAdapter;
import com.kuanquan.doublelistrecyclerview.bean.ClassModel;
import com.kuanquan.doublelistrecyclerview.bean.StudentModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRvClass;
    private RecyclerView mRvStudent;
    private ClassAdapter classAdapter;
    private StudentAdapter studentAdapter;
    private String jsonData;
    private List<StudentModel> studentList;
    private List<ClassModel> classAllList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simulationsData();
        initView();
        initData();
        initAdapter();
    }

    private void initView() {
        mRvClass = findViewById(R.id.rv_class);
        mRvStudent = findViewById(R.id.rv_student);
    }

    private void initData() {
        /**
         * 解析数据
         * */
        studentList = new ArrayList<>();
        classAllList = JSON.parseArray(jsonData, ClassModel.class);

        /**
         * 设置默认数据
         * */
        studentList.clear();
        for (int i = 0; i < classAllList.size(); i++) {
            studentList.addAll(classAllList.get(i).getList());
        }
    }

    private void initAdapter() {
        classAdapter = new ClassAdapter(classAllList);
        mRvClass.setLayoutManager(new LinearLayoutManager(this));
        mRvClass.setAdapter(classAdapter);
        //设置默认的选取状态
        classAdapter.setSelection(0);

        studentAdapter = new StudentAdapter(studentList);
        mRvStudent.setLayoutManager(new LinearLayoutManager(this));
        mRvStudent.setAdapter(studentAdapter);

        /**
         * 左侧列表的事件处理
         * */
        classAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                classAdapter.setSelection(position);
                //临时变量记录当前左侧选中条目的子集数据长度
                int sum = 0;
                for (int i = 0; i < position; i++) {
                    sum += classAllList.get(i).getList().size();
                }
                //根据左侧，定位右侧的展示数据
                LinearLayoutManager layoutManager = (LinearLayoutManager) mRvStudent.getLayoutManager();
                layoutManager.scrollToPositionWithOffset(sum, 0);
            }
        });

        mRvStudent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //获取滚动时的第一条展示的position
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                //获取右侧数据的关联id
                StudentModel studentModel = studentList.get(firstVisibleItemPosition);
                int outId = studentModel.getOutId();
                //记录外部id， 更新左侧状态栏状态
                int pos = 0;
                for (int i = 0; i < classAllList.size(); i++) {
                    int id = classAllList.get(i).getId();
                    if ((outId == id)) {
                        pos = i;
                    }
                }
                classAdapter.setSelection(pos);
            }
        });
    }

    /**
     * 数据模拟 - 可忽略 ，一般这里的数据都是后台网络请求之后返回的
     */
    private void simulationsData() {
        List<ClassModel> classList = new ArrayList<>();
        List<StudentModel> studentList1 = new ArrayList<>();
        List<StudentModel> studentList2 = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            StudentModel studentModel = new StudentModel();
            studentModel.setName("小明" + i);
            studentModel.setAge(i);
            studentModel.setOutId(1);
            studentList1.add(studentModel);
        }

        for (int i = 0; i < 20; i++) {
            StudentModel studentModel = new StudentModel();
            studentModel.setName("小红" + i);
            studentModel.setAge(i);
            studentModel.setOutId(2);
            studentList2.add(studentModel);
        }

        ClassModel classModel = new ClassModel();
        classModel.setList(studentList1);
        classModel.setId(1);
        classModel.setName("一年级");

        ClassModel classMode2 = new ClassModel();
        classMode2.setList(studentList2);
        classMode2.setId(2);
        classMode2.setName("二年级");
        classList.add(classModel);
        classList.add(classMode2);

        jsonData = JSON.toJSONString(classList);
        Log.e("tag", jsonData);
    }
}
