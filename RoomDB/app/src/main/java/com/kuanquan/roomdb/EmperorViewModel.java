package com.kuanquan.roomdb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kuanquan.roomdb.db.MyDatabase;
import com.kuanquan.roomdb.model.Emperor;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EmperorViewModel extends AndroidViewModel {

    private MyDatabase myDatabase;
    private LiveData<List<Emperor>> listEmperor;

    public EmperorViewModel(@NonNull @NotNull Application application) {
        super(application);
        // 初始化数据
        myDatabase = MyDatabase.getInstance(application);
        listEmperor = myDatabase.getEmperorDao().queryEmperorsByLiveData();
    }

    public LiveData<List<Emperor>> getListEmperor() {
        return listEmperor;
    }
}
