package com.kuanquan.roomdb.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kuanquan.roomdb.model.Emperor;

import java.util.List;

//注解Dao

@Dao
public interface EmperorDao {

    // 插入数据
    @Insert
    void insertEmperor(Emperor... emperor);

    // 删除数据
    @Delete
    void deleteEmperor(Emperor... emperor);

    // 修改数据
    @Update
    void updateEmperor(Emperor... emperor);

    // 查符合指定条件的某一个
    @Query("SELECt * FROM emperor_table WHERE id=:id")
    Emperor queryEmperorById(int id);

    // 查所有符合条件的
    @Query("SELECt * FROM emperor_table WHERE age=:age")
    List<Emperor> queryEmperorsByAge(String age);

    // 不设置条件，查询所有数据
    @Query("SELECt * FROM emperor_table")
    List<Emperor> queryEmperors();

    // 查所有，使用LiveData将结果List<Emperor>包装起来
    @Query("SELECt * FROM emperor_table")
    LiveData<List<Emperor>> queryEmperorsByLiveData();
}
