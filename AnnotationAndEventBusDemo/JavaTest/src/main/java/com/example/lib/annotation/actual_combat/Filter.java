package com.example.lib.annotation.actual_combat;

/**
 * 注解实战
 * <p>
 * 实现一个持久层框架，用来代替Hibernate的解决方案，核心代码是通过注解来实现。
 * 需求：
 * 1.有一张用户表，包括用户ID、用户名、昵称、年龄、性别、所在城市、邮箱、手机号
 * 2.方便的对每个字段或字段的组合条件进行检索，打印出SQL。
 * 3.使用方式要足够简单
 * Filter类：
 */
@Table("user")
public class Filter {

    @Column("id")
    private int id;

    @Column("userName")
    private String userName;

    @Column("nickName")
    private String nickName;

    @Column("age")
    private int age;

    @Column("city")
    private String city;

    @Column("email")
    private String email;

    @Column("mobile")
    private String mobile;


    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getNickName() {
        return nickName;
    }

    public int getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }


}
