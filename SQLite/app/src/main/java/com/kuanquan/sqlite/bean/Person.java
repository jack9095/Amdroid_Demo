package com.kuanquan.sqlite.bean;
/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-08-23
 * Time: 15:57
 * Description:
 */
public class Person {
	public int id;
	public String name;
	public String phonenum;

	public Person(int id, String name, String phonenum) {
		this.id = id;
		this.name = name;
		this.phonenum = phonenum;
	}

	@Override
	public String toString() {
		return "Person{" +
				"id=" + id +
				", name='" + name + '\'' +
				", phonenum='" + phonenum + '\'' +
				'}';
	}
}
