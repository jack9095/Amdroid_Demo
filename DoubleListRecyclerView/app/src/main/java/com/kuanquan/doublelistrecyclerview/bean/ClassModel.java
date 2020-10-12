package com.kuanquan.doublelistrecyclerview.bean;

import java.util.List;

public class ClassModel {
    private int id;
    private String name;
    private List<StudentModel> list;

    public ClassModel() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StudentModel> getList() {
        return list;
    }

    public void setList(List<StudentModel> list) {
        this.list = list;
    }
}
