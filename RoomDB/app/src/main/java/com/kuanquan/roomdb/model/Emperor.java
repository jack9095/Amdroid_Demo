package com.kuanquan.roomdb.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * 建表
 */
// Entity注解将Emperor类和数据库中的表emperor_table对应起。
// tableName可以指定别名，亦可不指定，若不指定就默认使用类名作为表名
@Entity(tableName = "emperor_table")
public class Emperor {

    //PrimaryKey主键，autoGenerate自增长
//    @PrimaryKey(autoGenerate = true)
    //ColumnInfo用于指定该字段存储在表中的名字，并指定类型
//    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
//    public int id;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    public int id;

    @ColumnInfo(name = "emperor_name", typeAffinity = ColumnInfo.TEXT)
    public String name;

    @ColumnInfo(name = "age", typeAffinity = ColumnInfo.INTEGER)
    public Integer age;

    //增加一个性别的字段, 数据库要升级
//    @ColumnInfo(name = "gender", typeAffinity = ColumnInfo.TEXT)
//    public String gender;

    //Room默认使用这个构造方法操作数据
    public Emperor(int id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    //Ignore亦可注解字段，让Room忽略此方法或者字段
    //由于Room只能识别一个构造器，如果需要定义多个构造器，可以使用Ignore注解让Room忽略这个构造器
    //Room不会持久化被注解Ignore标记过的字段
    @Ignore
    public Emperor(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

//    @Ignore
//    public Emperor(int id, String name, Integer age, String gender) {
//        this.id = id;
//        this.name = name;
//        this.age = age;
//        this.gender = gender;
//    }

    @Override
    public String toString() {
        return "Emperor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }

}
